package com.example.administrator.mysocialapp;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by Administrator on 2017/4/5.
 */

public class MyRefreshLayout extends SwipeRefreshLayout implements AbsListView.OnScrollListener {

    private ListView listView;
    //正在加载中 标志
    private boolean loading = false;
    //接口对象 用于调用 接口的实现方法
    private LoadListener loadListener;

    /**
     * 设置监听
     *
     * @param loadListener
     */
    public void setLoadListener(LoadListener loadListener) {
        this.loadListener = loadListener;
    }

    //--------------继承SwipeRefreshLayout所实现的两个方法------------------------
    public MyRefreshLayout(Context context) {
        super(context);
    }

    public MyRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //------------------------------------------------------------------------
    //1.根据滑动来操作   onLayout是布局  onMeasure测量  onDraw绘制

    //①布局
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //获取子控件个数
        int childs = getChildCount();
        //判断子控件至少有一个
        if (childs > 0) {
            //获取第一个子控件
            View child = getChildAt(0);
            //判断  第一个子控件  是不是  ListView对象
            if (child instanceof ListView) {
                listView = (ListView) child;  //拿到第一个子控件
                listView.setOnScrollListener(this);  //设置滑动监听
            }
        }

    }

    /****************
     * 测量  绘制 不用写 知道即可
     ********************/
    //②测量
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //③绘制
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
    //------------------------------------------------------------------------
    //-------实现的setOnScrollListener接口-------

    /**
     * 滚动状态改变
     *
     * @param view
     * @param scrollState
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == 0) {
            canLoad();
        }
    }

    /**
     * 滚动
     *
     * @param view
     * @param firstVisibleItem
     * @param visibleItemCount
     * @param totalItemCount
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
    //------------------------------------------------------------------------

    /**
     * 是否可以 加载更多
     *
     * @return
     */
    private boolean canLoad() {
        if (isBottom() && !loading) {
            load();
        }
        return false;
    }

    /**
     * 加载
     */
    private void load() {

        if (loadListener != null) {
            setLoadingView(true);
            loadListener.load();//用来实现加载数据的方法
        }
    }

    /**
     * 设置 加载更多  是否可见
     *
     * @param isLoading true 可见 false 不可见
     */
    public void setLoadingView(boolean isLoading) {
        loading = isLoading; //开始加载
        loadListener.setFootView(isLoading);//实现可见的
    }

    /**
     * 判断 listView 是否 已经到底部
     *
     * @return
     */
    private boolean isBottom() {
        //判断  listView不为空 并且 有 数据内容
        if (listView != null && listView.getAdapter().getCount() > 0) {
            //判断 当前最后一个可见的item下标 是否 是 listView的最后一个item
            if (listView.getLastVisiblePosition() == listView.getAdapter().getCount() - 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 上拉 加载更多接口
     */
    public interface LoadListener {
        //用于加载数据
        void load();

        //用于设置 加载更多 是否可见
        void setFootView(boolean loading);
    }
}
