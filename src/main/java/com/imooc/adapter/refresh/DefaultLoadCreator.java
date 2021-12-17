package com.imooc.adapter.refresh;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.imooc.adapter.R;


/**
 * 默认样式的底部加载
 */
public class DefaultLoadCreator extends LoadViewCreator {


    @Override
    public View getLoadView(Context context, ViewGroup parent) {
        View refreshView = LayoutInflater.from(context).inflate(R.layout.layout_load_view,parent,false);
        return refreshView;
    }

    @Override
    public void onPull(int currentDragHeight, int refreshViewHeight, int currentRefreshStatus) {

        Log.e("TAG","onPull");
    }

    @Override
    public void onLoading() {
        Log.e("TAG","onLoading");
    }

    @Override
    public void onStopLoad() {
        Log.e("TAG","onStopLoad");
    }
}
