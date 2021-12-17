package com.imooc.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 打造通用Recycler 适配器
 * @param <T>
 */
public abstract class CommonRecyclerAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    public Context mContext;
    private LayoutInflater mInflater;
    // 数据采用泛型
    private List<T> mDatas;
    // 布局构造函数传入
    private int mLayoutId;

    // 多布局支持
    private MultiTypeSupport mMultiTypeSupport;

    /***************
     * 设置条目点击和长按事件
     *********************/
    public OnItemClickListener mItemClickListener;
    public OnItemLongClickListener mLongClickListener;

    public CommonRecyclerAdapter(Context context, List<T> datas, int layoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mDatas = datas;
        this.mLayoutId = layoutId;
    }
    public CommonRecyclerAdapter(Context context, List<T> datas, MultiTypeSupport multiTypeSupport) {
        this(context,datas,-1);
        this.mMultiTypeSupport = multiTypeSupport;
    }


    /**
     * 根据当前位置获取不同的viewType
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (mMultiTypeSupport!=null){
            return mMultiTypeSupport.getLayoutId(mDatas.get(position),position);
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 多布局支持
        if (mMultiTypeSupport!=null){
            mLayoutId = viewType;
        }
        View view = mInflater.inflate(mLayoutId,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(position);
                }
            });
        }
        if (mLongClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mLongClickListener.onLongClick(position);
                }
            });
        }
        convert(holder,mDatas.get(position-1));
    }


    /**
     *  利用抽象方法回传出去，每个不一样的Adapter去设置
     * @param holder
     * @param item
     */
    public abstract void convert(ViewHolder holder, T item);

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public void setLongClickListener(OnItemLongClickListener longClickListener) {
        this.mLongClickListener = longClickListener;
    }
}
