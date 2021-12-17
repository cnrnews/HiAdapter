package com.imooc.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView.ViewHolder 封装
 */
public class ViewHolder extends RecyclerView.ViewHolder {

    // 缓存布局view 减少findViewById 次数
    private SparseArray<View> mViews;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
    }

    /**
     * 设置view显示/隐藏
     * @param viewId
     * @param visibility
     * @return
     */
    public ViewHolder setViewVisibility(int viewId,int visibility){
        View tv = getView(viewId);
        tv.setVisibility(visibility);
        return this;
    }
    /**
     * 设置 ImageView的资源
     * @param viewId
     * @param resourceId
     * @return
     */
    public ViewHolder setImageResource(int viewId,int resourceId){
        ImageView imageView =getView(viewId);
        imageView.setImageResource(resourceId);
        return this;
    }

    /**
     * 设置 点击事件
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId,String text){
        TextView tv =getView(viewId);
        tv.setText(text);
        return this;
    }
    /**
     * 设置 点击事件
     * @param listener
     * @return
     */
    public ViewHolder setOnIntemClickListener(View.OnClickListener listener){
        itemView.setOnClickListener(listener);
        return this;
    }
    /**
     * 设置 长按事件
     * @param viewId
     * @param listener
     * @return
     */
    public ViewHolder setOnIntemLongClickListener(int viewId, View.OnLongClickListener listener){
        TextView tv =getView(viewId);
        tv.setOnLongClickListener(listener);
        return this;
    }
    /**
     * 设置图片通过路径,这里稍微处理得复杂一些，因为考虑加载图片的第三方可能不太一样
     * 也可以直接写死
     */
    public ViewHolder setImageByUrl(int viewId, HolderImageLoader imageLoader) {
        ImageView imageView = getView(viewId);
        if (imageLoader == null) {
            throw new NullPointerException("imageLoader is null!");
        }
        imageLoader.displayImage(imageView.getContext(), imageView, imageLoader.getImagePath());
        return this;
    }

    /**
     * 图片加载，这里稍微处理得复杂一些，因为考虑加载图片的第三方可能不太一样
     * 也可以不写这个类
     */
    public static abstract class HolderImageLoader {
        private String mImagePath;

        public HolderImageLoader(String imagePath) {
            this.mImagePath = imagePath;
        }

        public String getImagePath() {
            return mImagePath;
        }

        public abstract void displayImage(Context context, ImageView imageView, String imagePath);
    }
    public  <T extends View> T getView(int viewId){
        // 先从缓存读取
        View view = mViews.get(viewId);
        if (view == null){
            view = itemView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return (T) view;
    }
}
