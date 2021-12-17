package com.imooc.adapter.refresh;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * 下拉刷新的辅助类为了匹配所有效果
 */
public abstract class RefreshViewCreator {

    /**
     * 获取下拉刷新的view
     * @param context
     * @param parent
     * @return
     */
    public abstract View getRefreshView(Context context, ViewGroup parent);

    /**
     * 正在下拉
     * @param currentDragHeight
     * @param refreshViewHeight
     * @param currentRefreshStatus
     */
    public abstract void onPull(int currentDragHeight, int refreshViewHeight, int currentRefreshStatus);

    /**
     * 正在刷新中
     */
    public abstract void onRefreshing();

    /**
     * 停止刷新
     */
    public abstract void onStopRefresh();
}
