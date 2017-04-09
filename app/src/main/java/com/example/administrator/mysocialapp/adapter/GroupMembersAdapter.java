package com.example.administrator.mysocialapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.mysocialapp.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;

import java.util.List;

/**
 * Created by Administrator on 2017/4/5.
 */

public class GroupMembersAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;

    public GroupMembersAdapter(Context context, String groupId) {
        // 根据群组ID从本地获取群组基本信息
        EMGroup group = EMClient.getInstance().groupManager().getGroup(groupId);
        // 获取 群成员
        this.list = group.getMembers();
        this.context = context;

        String owner = group.getOwner();
        this.list.remove(owner);
        // 获取群主 并添加到 集合的 第一个位置
        this.list.add(0, group.getOwner());
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    ViewHolder viewHolder;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_group_members, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.item_group_member_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String nameStr = (String) getItem(position);
        viewHolder.name.setText(nameStr);

        if (0 == position) {
            viewHolder.name.setTextColor(context.getResources().getColor(R.color.red));
        } else {
            viewHolder.name.setTextColor(context.getResources().getColor(R.color.blue));
        }
        return convertView;
    }

    private class ViewHolder {
        private TextView name;
    }
}
