package com.imooc.adapter.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class WrapRecycler extends RecyclerView {
    // 增加一些通用功能
    // 空列表数据应该显示的空View
    // 正在加载数据页面，也就是正在获取后台接口页面
    private View mEmptyView, mLoadingView;

    // 包裹了一层的头部底部Adapter
    private WrapRecyclerAdapter mWrapRecyclerAdapter;
    // 这个是列表数据的Adapter
    private Adapter mAdapter;

    public WrapRecycler(@NonNull Context context) {
        this(context,null);
    }

    public WrapRecycler(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public WrapRecycler(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 装饰着模式应用的地方
     * @param adapter
     */
    @Override
    public void setAdapter(Adapter adapter){
        // 为了防止多次设置adapter
        if (mAdapter!=null){
            mAdapter.unregisterAdapterDataObserver(mAdapterObserver);
            mAdapter = null;
        }
        this.mAdapter = adapter;
        if (adapter instanceof WrapRecyclerAdapter){
            mWrapRecyclerAdapter = (WrapRecyclerAdapter) adapter;
        }else{
            mWrapRecyclerAdapter = new WrapRecyclerAdapter(adapter);
        }

        super.setAdapter(mWrapRecyclerAdapter);

        // 注册一个观察者
        mAdapter.registerAdapterDataObserver(mAdapterObserver);
        // 解决GridLayout添加头部和底部也要占据一行
        mWrapRecyclerAdapter.adjustSpanSize(this);
    }

    /**
     * 添加头部
     * @param view
     */
    public void addHeaderView(View view){
        if (mWrapRecyclerAdapter == null) return;
        mWrapRecyclerAdapter.addHeaderView(view);
    }

    /**
     * 添加底部
     * @param view
     */
    public void addFooterView(View view){
        if (mWrapRecyclerAdapter == null) return;
        mWrapRecyclerAdapter.addFooterView(view);
    }

    /**
     * 移除头部
     * @param view
     */
    public void removeHeaderView(View view){
        if (mWrapRecyclerAdapter == null) return;
        mWrapRecyclerAdapter.removeHeaderView(view);
    }
    /**
     * 移除尾部
     * @param view
     */
    public void removeFooterView(View view){
        if (mWrapRecyclerAdapter == null) return;
        mWrapRecyclerAdapter.removeFooterView(view);
    }

    private AdapterDataObserver mAdapterObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            if (mAdapter==null)return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyDataSetChanged没效果
            if (mWrapRecyclerAdapter!=mAdapter){
                mWrapRecyclerAdapter.notifyDataSetChanged();
            }
            dataChanged();
        }
        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyDataSetChanged没效果
            if (mWrapRecyclerAdapter != mAdapter)
                mWrapRecyclerAdapter.notifyItemRemoved(positionStart);
            dataChanged();
        }
        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemMoved没效果
            if (mWrapRecyclerAdapter != mAdapter)
                mWrapRecyclerAdapter.notifyItemMoved(fromPosition, toPosition);
            dataChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemChanged没效果
            if (mWrapRecyclerAdapter != mAdapter)
                mWrapRecyclerAdapter.notifyItemChanged(positionStart);
            dataChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemChanged没效果
            if (mWrapRecyclerAdapter != mAdapter)
                mWrapRecyclerAdapter.notifyItemChanged(positionStart, payload);
            dataChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyItemInserted没效果
            if (mWrapRecyclerAdapter != mAdapter)
                mWrapRecyclerAdapter.notifyItemInserted(positionStart);
            dataChanged();
        }
    };

    /**
     * 添加一个空列表数据页面
     * @param emptyView
     */
    public void addEmptyView(View emptyView){
        this.mEmptyView = emptyView;
    }

    /**
     *添加一个正在加载数据的页面
     * @param loadingView
     */
    public void addLoadingView(View loadingView){
        this.mLoadingView = loadingView;
    }

    /**
     * Adapter数据改变的方法
     */
    private void dataChanged(){
        if (mAdapter.getItemCount() == 0){
            if (mEmptyView!=null){
                mEmptyView.setVisibility(VISIBLE);
            }else{
                mEmptyView.setVisibility(GONE);
            }
        }
    }
}
