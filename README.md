# HiAdapter

#### 介绍
RecyclerView 打造万能Adapter,支持自定义Header，Footer，上拉刷新，下拉加载

#### 软件架构
1.使用【装饰者模式】扩展RecyclerView 添加自定义Header，Footer
2.使用【泛型】适配多种接口请求实体

#### 使用说明

1.  基本使用

```
CommonRecyclerAdapter adapter = new CommonRecyclerAdapter<Article.DataBean.DatasBean>(this,result.data.datas,R.layout.layout_article) {
            @Override
            public void convert(ViewHolder holder, Article.DataBean.DatasBean item) {
                holder.setText(R.id.title, item.title);
                holder.setText(R.id.chapter, item.chapterName);
                holder.setText(R.id.time, item.niceDate);
            }
        };
mRv.setAdapter(adapter);
adapter.setItemClickListener(new OnItemClickListener() {  
    @Override
    public void onItemClick(int position) {
       Toast.makeText(CommonAdapterActivity.this,"点击了"+position,Toast.LENGTH_SHORT).show();
    }
 });
```
2.  添加自定义Header 和 Footer

```
View headerView = LayoutInflater.from(this).inflate(R.layout.layout_article_header,mRv,false);
mRv.addHeaderView(headerView);
```

3.  下拉刷新，上拉加载更多

```
 <com.imooc.adapter.refresh.LoadRefreshRecyclerView
        android:id="@+id/recycler_refresh"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
LoadRefreshRecyclerView mRv
// 自定义上拉加载更多布局
mRv.addLoadViewCreator();
// 自定义下拉刷新布局
mRv.addRefreshViewCreator();
// 添加进入页面加载View
mRv.addLoadingView();
// 自定义加载空布局
mRv.addEmptyView();

```
4.  RecyclerView 添加拖拽删除

```
  <com.imooc.adapter.refresh.WrapRecycler
        android:id="@+id/recycler"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>

 WrapRecycler mRv

 ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                // 获取触摸响应的方向   包含两个 1.拖动dragFlags 2.侧滑删除swipeFlags
                // 代表只能是向左侧滑删除，当前可以是这样ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT
                int swipeFlags = ItemTouchHelper.LEFT;

                // 拖动
                int dragFlags = 0;
                if (recyclerView.getLayoutManager() instanceof GridLayoutManager){
                    // GridView 样式四个方向都可以
                    dragFlags = ItemTouchHelper.LEFT |ItemTouchHelper.RIGHT|ItemTouchHelper.UP|ItemTouchHelper.DOWN;
                }else{
                    // ListView 样式不支持左右，只支持上下
                    dragFlags = ItemTouchHelper.UP |
                            ItemTouchHelper.DOWN;
                }

                // 拖动暂不处理默认是0
                return makeMovementFlags(dragFlags,swipeFlags);
            }

            /**
             * 拖动回调
             * @param recyclerView
             * @param viewHolder
             * @param target
             * @return
             */
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                // 获取原来的位置
                int fromPos = viewHolder.getAdapterPosition();
                int targetPos = target.getAdapterPosition();
                if (fromPos>targetPos){
                    // 上移
                    for (int i = fromPos; i < targetPos; i++) {
                        Collections.swap(mItems,i,i+1);
                    }
                }else{
                    // 下移
                    for (int i = fromPos; i > targetPos; i--) {
                        Collections.swap(mItems,i,i-1);
                    }
                }
                mAdapter.notifyItemMoved(fromPos,targetPos);
                return true;
            }

            /**
             * 侧滑删除之后的回调方法
             * @param viewHolder
             * @param direction
             */
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // 获取当前删除的位置
                int position = viewHolder.getAdapterPosition();
                // 删除数据
                mItems.remove(position);
                // adapter 更新notify当前位置删除
                mAdapter.notifyItemRemoved(position);
            }
            /**
             * 拖动选择状态改变回调
             */
            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE){
                    //侧滑或者拖动的时候背景设置为灰色
                    viewHolder.itemView.setBackgroundColor(Color.GRAY);
                }
            }
            /**
             * 回到正常状态的时候回调
             */
            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                // 正常默认状态下背景恢复默认
                viewHolder.itemView.setBackgroundColor(0);
                viewHolder.itemView.setTranslationX(0);
            }
        });
        // 绑定RecyclerView
        itemTouchHelper.attachToRecyclerView(mRv);
```



