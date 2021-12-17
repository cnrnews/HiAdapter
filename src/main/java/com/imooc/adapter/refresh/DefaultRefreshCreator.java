package com.imooc.adapter.refresh;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.imooc.adapter.R;


/**
 * 默认样式的头部刷新
 */
public class DefaultRefreshCreator extends RefreshViewCreator {

    // 加载数据的ImageView
    private View mRefreshIv;
    private TextView mRefreshTv;

    @Override
    public View getRefreshView(Context context, ViewGroup parent) {
        View refreshView = LayoutInflater.from(context).inflate(R.layout.layout_refresh_header_view,parent,false);
        mRefreshIv = refreshView.findViewById(R.id.refresh_iv);
        mRefreshTv =  refreshView.findViewById(R.id.refresh_tv);
        return refreshView;
    }

    @Override
    public void onPull(int currentDragHeight, int refreshViewHeight, int currentRefreshStatus) {

        Log.e("TAG","onPull");


        float rotate = ((float)currentDragHeight)/refreshViewHeight;
        // 不断下拉的过程中不断的旋转图片
        mRefreshTv.setText("放开加载数据...");
        mRefreshIv.setRotation(rotate*360);
    }

    @Override
    public void onRefreshing() {
        Log.e("TAG","onRefreshing");

        mRefreshTv.setText("加载数据中...");
        // 刷新的时候不断旋转
        RotateAnimation animation = new RotateAnimation(0,720,
                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation.setRepeatCount(-1);
        animation.setDuration(1000);
        mRefreshIv.startAnimation(animation);
    }

    @Override
    public void onStopRefresh() {
        Log.e("TAG","onStopRefresh");
        mRefreshTv.setVisibility(View.GONE);
        // 停止加载的时候清除动画
        mRefreshIv.setRotation(0);
        mRefreshIv.clearAnimation();
    }
}
