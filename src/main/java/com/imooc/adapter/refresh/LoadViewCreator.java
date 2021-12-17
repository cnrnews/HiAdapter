package com.imooc.adapter.refresh;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * 上拉加载更多的辅助类为了匹配所有效果
 */
public abstract class LoadViewCreator {

    /**
     * 获取上拉加载更多的View
     * @param context
     * @param parent
     * @return
     */
    public abstract View getLoadView(Context context, ViewGroup parent);

    /**
     * 正在下拉
     * @param currentDragHeight
     * @param refreshViewHeight
     * @param currentRefreshStatus
     */
    public abstract void onPull(int currentDragHeight, int refreshViewHeight, int currentRefreshStatus);

    /**
     * 正在加载中
     */
    public abstract void onLoading();

    /**
     * 停止加载
     */
    public abstract void onStopLoad();
}
