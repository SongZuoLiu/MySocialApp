package com.example.administrator.mysocialapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.mysocialapp.R;

/**
 * Created by Administrator on 2017/4/1.
 * <p>
 * ExpandableList的适配器
 */

public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context context;

    /* 布局填充器*/
    private LayoutInflater inflater;
    private String[] group = new String[]{"我的好友", "我的同学", "我的同事", "我的朋友"};
    private String[][] childs = new String[][]{{"林动", "绫清竹", "应欢欢", "林青檀", "林静"},
            {"箫炎", "萧熏儿", "彩鳞", "纳兰嫣然", "云韵", "小医仙", "青鳞", "雅妃", "萧潇", "琥嘉", "紫妍", "药老"},
            {"林逸", "楚梦瑶", "陈雨舒", "雨凝", "唐韵", "冰糖"},
            {"马云", "李彦宏", "周鸿祎", "雷军", "马化腾", "刘强东", "王氏", "潘石屹"}};


    /* 构造,因为布局填充器填充布局需要使用到Context,通过构造来传递 */
    public MyExpandableListViewAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    /*组数*/
    @Override
    public int getGroupCount() {
        return group.length;
    }

    /* 指定组的子Item数*/
    @Override
    public int getChildrenCount(int groupPosition) {
        return childs[groupPosition].length;
    }

    /*组数据*/
    @Override
    public Object getGroup(int groupPosition) {
        return group[groupPosition];
    }

    /*返回子选项的数据*/
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childs[groupPosition][childPosition];
    }

    /*组ID*/
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /*子ID*/
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /*ID是否唯一*/
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /* 组选项的视图处理 */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        /* 填充布局*/
        View view = inflater.inflate(R.layout.item_elv_group, null);
        ImageView iv_group_icon = (ImageView) view.findViewById(R.id.iv_group_icon);
        TextView tv_group_name = (TextView) view.findViewById(R.id.tv_group_name);
        TextView tv_group_number = (TextView) view.findViewById(R.id.tv_group_number);

        tv_group_name.setText(group[groupPosition]);
        tv_group_number.setText(childs[groupPosition].length + "/" + childs[groupPosition].length);

        /*isExpanded 子列表是否展开*/
        if (isEmpty()) {
            iv_group_icon.setImageResource(R.drawable.arrow_down);
        } else {
            iv_group_icon.setImageResource(R.drawable.arrow);
        }
        return view;
    }

    /* 子选项的视图处理 */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_elv_child, null);

        ImageView iv_child_icon = (ImageView) view.findViewById(R.id.iv_child_icon);
        TextView tv_child_info = (TextView) view.findViewById(R.id.tv_child_info);
        TextView tv_child_name = (TextView) view.findViewById(R.id.tv_child_name);
        TextView tv_child_network = (TextView) view.findViewById(R.id.tv_child_network);

        tv_child_name.setText(childs[groupPosition][childPosition]);
        tv_child_network.setText(childPosition % 2 == 0 ? "5G" : "4G");
        return view;
    }

    /* 子选项的视图处理 */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
