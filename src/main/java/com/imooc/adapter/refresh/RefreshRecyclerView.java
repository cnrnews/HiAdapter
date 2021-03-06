package com.imooc.adapter.refresh;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 下拉刷新的RecyclerView
 */
public class RefreshRecyclerView extends WrapRecycler {

    // 下拉刷新的辅助类
    private RefreshViewCreator mRefreshCreator = new DefaultRefreshCreator();
    // 下拉刷新头部的高度
    private int mRefreshViewHeight = 0;
    // 下拉刷新的头部View
    private View mRefreshView;
    // 手指按下的Y位置
    private int mFingerDownY;
    // 手指拖拽的阻力指数
    private float mDragIndex = 0.35f;
    // 当前是否正在拖动
    private boolean mCurrentDrag = false;
    // 当前的状态
    private int mCurrentRefreshStatus;
    // 默认状态
    public int REFRESH_STATUS_NORMAL = 0x0011;
    // 下拉刷新状态
    public int REFRESH_STATUS_PULL_DOWN_REFRESH = 0x0022;
    // 松开刷新状态
    public int REFRESH_STATUS_LOOSEN_REFRESHING = 0x0033;
    // 正在刷新状态
    public int REFRESH_STATUS_REFRESHING = 0x0033;

    public RefreshRecyclerView(Context context) {
        super(context);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void addRefreshViewCreator(RefreshViewCreator refreshViewCreator){
        this.mRefreshCreator = refreshViewCreator;
        addRefreshView();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        addRefreshView();
    }

    /**
     * 记录手指按下的位置 ,之所以写在dispatchTouchEvent那是因为如果我们处理了条目点击事件，
     * 那么就不会进入onTouchEvent里面，所以只能在这里获取
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mFingerDownY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                if (mCurrentDrag){
                    restoreRefreshView();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     *  重置当前刷新状态状态
     */
    private void restoreRefreshView() {
        int currentTopMargin = ((MarginLayoutParams) mRefreshView.getLayoutParams()).topMargin;
        int finalTopMargin = -mRefreshViewHeight + 1;
        if (mCurrentRefreshStatus == REFRESH_STATUS_LOOSEN_REFRESHING){
            finalTopMargin = 0;
            mCurrentRefreshStatus = REFRESH_STATUS_REFRESHING;
            if (mRefreshCreator!=null){
                mRefreshCreator.onRefreshing();
            }
            if (mListener!=null){
                mListener.onRefresh();
            }
        }
        int distance = currentTopMargin = finalTopMargin;

        // 回弹到指定位置
       ValueAnimator animator= ObjectAnimator.ofFloat(currentTopMargin,finalTopMargin).setDuration(distance);
       animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
           @Override
           public void onAnimationUpdate(ValueAnimator valueAnimator) {
               float currentTopMargin = (float) animator.getAnimatedValue();
               setRefreshViewMarginTop((int)currentTopMargin);
           }
       });
       animator.start();
       mCurrentDrag = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()){
            case MotionEvent.ACTION_MOVE:
                // 如果是在最顶部才处理，否则不需要处理
                if (canScrollUp() || mCurrentRefreshStatus == REFRESH_STATUS_REFRESHING){
                    // 如果没有到达最顶端，也就是说还可以向上滚动就什么都不处理
                    return super.onTouchEvent(e);
                }

                // 解决下拉刷新自动滚动问题
                if (mCurrentDrag){
                    scrollToPosition(0);
                }

                // 获取手指触摸拖拽的距离
                int distanceY = (int) ((e.getRawY() - mFingerDownY) * mDragIndex);
                // 如果是已经到达头部，并且不断的向下拉，那么不断的改变refreshView的marginTop的值
                if (distanceY>0){
                    int marginTop = distanceY - mRefreshViewHeight;
                    setRefreshViewMarginTop(marginTop);
                    updateRefreshStatus(marginTop);
                    mCurrentDrag = true;
                    return false;
                }

                break;
        }
        return super.onTouchEvent(e);
    }

    /**
     * 更新刷新的状态
     * @param marginTop
     */
    private void updateRefreshStatus(int marginTop) {
        if (marginTop <= -mRefreshViewHeight) {
            mCurrentRefreshStatus = REFRESH_STATUS_NORMAL;
        } else if (marginTop < 0) {
            mCurrentRefreshStatus = REFRESH_STATUS_PULL_DOWN_REFRESH;
        } else {
            mCurrentRefreshStatus = REFRESH_STATUS_LOOSEN_REFRESHING;
        }

        if (mRefreshCreator != null) {
            mRefreshCreator.onPull(marginTop, mRefreshViewHeight, mCurrentRefreshStatus);
        }
    }

    /**
     * 判断是不是滚动到了最顶部，这个是从SwipeRefreshLayout里面copy过来的源代码
     * @return
     */
    private boolean canScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            return canScrollVertically( -1) || this.getScrollY() > 0;
        } else {
            return canScrollVertically( -1);
        }
    }

    /**
     * 设置刷新View的marginTop
     * @param marginTop
     */
    private void setRefreshViewMarginTop(int marginTop) {
        MarginLayoutParams params = (MarginLayoutParams) mRefreshView.getLayoutParams();
        if (marginTop<-mRefreshViewHeight+1){
            marginTop = -mRefreshViewHeight+1;
        }
        params.topMargin = marginTop;
        mRefreshView.setLayoutParams(params);
    }

    /**
     *  添加头部的刷新View
     */
    private void addRefreshView() {
        Adapter adapter = getAdapter();
        if (adapter!=null&&mRefreshCreator!=null){
            // 添加头部的刷新view
            View refreshView = mRefreshCreator.getRefreshView(getContext(), this);
            if (refreshView!=null){
                addHeaderView(refreshView);
                this.mRefreshView = refreshView;
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed){
            if (mRefreshView!=null && mRefreshViewHeight <=0){
                // 获取头部刷新View的高度
                mRefreshViewHeight = mRefreshView.getMeasuredHeight();
                if (mRefreshViewHeight>0){
                    // 隐藏头部刷新的View  marginTop  多留出1px防止无法判断是不是滚动到头部问题
                    setRefreshViewMarginTop(-mRefreshViewHeight+1);
                }
            }
        }
    }

    /**
     * 停止刷新
     */
    public void onStopRefresh(){
        mCurrentRefreshStatus = REFRESH_STATUS_NORMAL;
        restoreRefreshView();
        if (mRefreshCreator!=null){
            mRefreshCreator.onStopRefresh();
        }
    }
    // 处理刷新回调监听
    private OnRefreshListener mListener;

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mListener = listener;
    }

    public interface OnRefreshListener {
        void onRefresh();
    }
}
