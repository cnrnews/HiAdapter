package com.imooc.adapter.refresh;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WrapRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // 数据列表的Adapter 不包含头部的
    private RecyclerView.Adapter mAdapter;

    // 头部和尾部的集合
    private SparseArray<View> mHeaders,mFooters;

    private static int BASE_HEADER_KEY = 1000000000;
    private static int BASE_FOOTER_KEY = 2000000000;

    public WrapRecyclerAdapter(RecyclerView.Adapter adapter) {
        this.mAdapter = adapter;
        mHeaders = new SparseArray<>();
        mFooters = new SparseArray<>();
    }

    /**
     * 创建多布局ViewHolder
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isHeaderViewType(viewType)){
            // 头部
            return createFooterHeaderViewHolder(mHeaders.get(viewType));
        }else if (isFooterViewType(viewType)){
            //底部
            return createFooterHeaderViewHolder(mFooters.get(viewType));
        }

        return mAdapter.onCreateViewHolder(parent,viewType);
    }
    /**
     * 创建头部和尾部的ViewHolder
     * @param view
     * @return
     */
    private RecyclerView.ViewHolder createFooterHeaderViewHolder(View view) {
        return new RecyclerView.ViewHolder(view){};
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int numHeaders = mHeaders.size();
        // 头部底部不用处理
        if (isHeaderPosition(position) || isFooterPosition(position)){
            return;
        }
        // Adapter
        final int adjPosition = position - numHeaders;
        int adapterCount;
        if (mAdapter!=null){
            adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount){
                mAdapter.onBindViewHolder(holder, position);
            }
        }
    }
    /**
     * 是否是头部类型
     * @param viewType
     * @return
     */
    private boolean isHeaderViewType(int viewType) {
        return mHeaders.indexOfKey(viewType) >= 0;
    }
    /**
     * 是否是底部类型
     * @param viewType
     * @return
     */
    private boolean isFooterViewType(int viewType) {
        return mFooters.indexOfKey(viewType)>=0;
    }

    /**
     * 是否是header位置
     * @param position
     * @return
     */
    private boolean isHeaderPosition(int position){
        return position< mHeaders.size();
    }
    /**
     * 是否是footer位置
     * @param position
     * @return
     */
    private boolean isFooterPosition(int position){
        return position >= mHeaders.size()+mAdapter.getItemCount();
    }




    @Override
    public int getItemViewType(int position) {
        int numHeaders = mHeaders.size();
        if (isHeaderPosition(position)){
            return mHeaders.keyAt(position);
        }
        // Adapter
        final int adjPosition = position - numHeaders;
        int adapterCount = 0;
        if (mAdapter!=null){
            adapterCount = mAdapter.getItemCount();
            if (adjPosition<adapterCount){
                return mAdapter.getItemViewType(adjPosition);
            }
        }

        return mFooters.keyAt(adjPosition - adapterCount);

    }

    @Override
    public int getItemCount() {
        return getHeaderSize() + mAdapter.getItemCount()+getFooterSize();
    }
    /**
     * 获取头部大小
     * @return
     */
    private int getHeaderSize(){
        return mHeaders.size();
    }
    /**
     * 获取底部大小
     * @return
     */
    private int getFooterSize(){
        return mFooters.size();
    }
    /**
     * 添加头部
     * @param view
     */
    public void addHeaderView(View view){
        // 没有存储该view
        if (mHeaders.indexOfValue(view)==-1){
            mHeaders.put(BASE_HEADER_KEY++,view);
            // 刷新列表
            notifyDataSetChanged();
        }
    }
    /**
     * 添加底部
     * @param view
     */
    public void addFooterView(View view){
        // 没有存储该view
        if (mFooters.indexOfValue(view)==-1){
            mFooters.put(BASE_FOOTER_KEY++,view);
            // 刷新列表
            notifyDataSetChanged();
        }
    }
    /**
     * 移除头部
     * @param view
     */
    public void removeHeaderView(View view){
        if (mHeaders.indexOfValue(view)>=0){
            mHeaders.removeAt(mHeaders.indexOfValue(view));
            // 刷新列表
            notifyDataSetChanged();
        }
    }
    /**
     * 移除底部
     * @param view
     */
    public void removeFooterView(View view){
        if (mFooters.indexOfValue(view)>=0){
            mFooters.removeAt(mFooters.indexOfValue(view));
            // 刷新列表
            notifyDataSetChanged();
        }
    }
    /**
     * 解决GridLayoutManager添加头部和底部不占用一行的问题
     *
     * @param recycler
     * @version 1.0
     */
    public void adjustSpanSize(RecyclerView recycler) {
        if (recycler.getLayoutManager() instanceof GridLayoutManager) {
            final GridLayoutManager layoutManager = (GridLayoutManager) recycler.getLayoutManager();
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    boolean isHeaderOrFooter =
                            isHeaderPosition(position) || isFooterPosition(position);
                    return isHeaderOrFooter ? layoutManager.getSpanCount() : 1;
                }
            });
        }
    }

}
