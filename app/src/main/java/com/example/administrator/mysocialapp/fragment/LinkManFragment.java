package com.example.administrator.mysocialapp.fragment;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.administrator.mysocialapp.R;
import com.example.administrator.mysocialapp.act.AddFriendActivity;
import com.example.administrator.mysocialapp.act.CreateGroupActivity;
import com.example.administrator.mysocialapp.act.PrivateMessageActivity;
import com.example.administrator.mysocialapp.adapter.MyExpandableListViewAdapter;

/**
 * 好友列表页
 */
public class LinkManFragment extends Fragment implements View.OnClickListener {
    private TextView tv_new_friend, tv_group_chat;
    private View view;
    //可拓展列表视图
    private ExpandableListView expandableListView;
    //ExpandableList适配器
    private MyExpandableListViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.activity_link_man_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        expandableListView = (ExpandableListView) view.findViewById(R.id.elv);
        //创建一个Adapter实例
        adapter = new MyExpandableListViewAdapter(getActivity());

        // 设置适配器
        expandableListView.setAdapter(adapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                startActivity(new Intent(getActivity(), PrivateMessageActivity.class));
                return true;
            }
        });

        tv_new_friend = (TextView) view.findViewById(R.id.tv_new_friend);
        tv_group_chat = (TextView) view.findViewById(R.id.tv_group_chat);
        tv_new_friend.setOnClickListener(this);
        tv_group_chat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_new_friend:
                Intent it = new Intent(getActivity(), AddFriendActivity.class);
                startActivity(it);
                break;
            case R.id.tv_group_chat:
                Intent intent = new Intent(getActivity(), CreateGroupActivity.class);
                startActivity(intent);
                break;
        }
    }
}
