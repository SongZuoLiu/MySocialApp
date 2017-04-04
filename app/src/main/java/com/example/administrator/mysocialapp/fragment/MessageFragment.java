package com.example.administrator.mysocialapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.mysocialapp.R;
import com.example.administrator.mysocialapp.act.BaseActivity;
import com.example.administrator.mysocialapp.act.LoginActivity;
import com.example.administrator.mysocialapp.act.PrivateMessageActivity;
import com.example.administrator.mysocialapp.adapter.ChatListAdapter;
import com.example.administrator.mysocialapp.callback.ListItemClick;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMConversationListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/1.
 * <p>
 * 消息列表页
 */
public class MessageFragment extends Fragment implements View.OnClickListener, EMCallBack, EMConversationListener,
        EMConnectionListener, AdapterView.OnItemLongClickListener, ListItemClick {
    private View view;
    private EditText username, count;
    private Button main_send, main_exit;
    private ListView main_list;
    private View main_Connection_la;
    ChatListAdapter cle;
    ArrayList<EMConversation> list = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return view = inflater.inflate(R.layout.activity_message_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListView();
    }

    private void initView() {
        username = (EditText) view.findViewById(R.id.username);
        count = (EditText) view.findViewById(R.id.count);
        main_send = (Button) view.findViewById(R.id.main_send);
        main_exit = (Button) view.findViewById(R.id.main_exit);

        main_list = (ListView) view.findViewById(R.id.main_list);
        main_send.setOnClickListener(this);
        main_exit.setOnClickListener(this);

        main_Connection_la = view.findViewById(R.id.main_Connection_lay);
        main_list.setOnItemLongClickListener(this);
        //main_list.setOnItemClickListener(this);

        //-----------------------下拉刷新---------------------------------------
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.Swipe_srf);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉刷新内容
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
    }

    //--------------------------------------------------------------
    // 加载数据
    private void initDate() {
        list.clear();
        // 获取所有会话
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        // 把map数组添加到ArrayList中
        for (EMConversation emc : conversations.values()) {
            list.add(emc);
        }
        sort();// 调用下方的方法(排序)
    }

    //--------------------------------------------------------------
    private void initListView() {
        // 调用加载数据方法
        initDate();
        // 实例化适配器
        cle = new ChatListAdapter(getActivity(), list);
        // 给listvie设置适配器
        main_list.setAdapter(cle);

        cle.setListItemClick(this);
    }

    //--------------------------------------------------------------
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_exit:// 退出按钮
                // 退出环信服务器 再次启动程序 会重新登陆
                EMClient.getInstance().logout(true);
                intentTwo(LoginActivity.class);// 返回登录界面
                break;
            case R.id.main_send:// 发送按钮
                // 调用发送文本消息的方法
                sendText();
                break;
        }
    }

    //--------------------------------------------------------------
    // 跳转页面方法
    private void intentTwo(Class<?> loginActivityClass) {
        Intent intent = new Intent(getActivity(), loginActivityClass);
        getActivity().startActivity(intent);
    }

    //--------------------------------------------------------------
    // 发送文本消息方法 只发送文本 单聊
    private void sendText() {
        // 创建一条文本消息，content为消息文字内容，
        // toChatUsername为对方用户或者群聊的id
        EMMessage message = EMMessage.createTxtSendMessage(count.getText().toString(), username.getText().toString());
        // 设置消息状态回调(与上述备注的2.无关)
        message.setMessageStatusCallback(this);
        // 发送消息
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    //--------------------------------------------------------------
    // 发送信息成功 回调此方法
    @Override
    public void onSuccess() {
        Toast.makeText(getActivity(), "发送消息成功！", Toast.LENGTH_SHORT).show();
    }

    // （回调的是EMCallBack实现的方法）
    // 发送信息失败 回调此方法
    @Override
    public void onError(int i, String s) {
        Toast.makeText(getActivity(), "发送消息失败！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgress(int i, String s) {

    }

    //--------------------------------------------------------------
    // 当会话个数有改变时 会触发此内容
    // 注： 会话个数无改变，内容有改变时 不会触发此方法
    @Override
    public void onCoversationUpdate() {
        // 调用加载数据方法
        initDate();
        // 运行UI线程
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                // 刷新listView
                cle.refAll(list);
            }
        });
    }

    //--------------------------------------------------------------
    // 与环信服务器连接成功后 调用
    @Override
    public void onConnected() {
        // 运行UI线程
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getActivity(), "连接成功", Toast.LENGTH_SHORT).show();
                // 给这个控件设置 可见状态 为 完全隐藏
                main_Connection_la.setVisibility(View.GONE);
            }
        });
    }

    // 与环信服务器连接断开后 调用   (这个目前还没跑通，没实现)
    @Override
    public void onDisconnected(int i) {
        // 运行UI线程
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                // 提示 连接已断开
                Toast.makeText(getActivity(), "您的网络处于断开状态", Toast.LENGTH_SHORT);
                /// 给 main_Connection_la 控件设置可见状态为 可见
                main_Connection_la.setVisibility(View.VISIBLE);
            }
        });
    }

    //--------------------------------------------------------------
    // listView的item 长按事件
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        // 获取点击事件的item内容数据
        EMConversation msg = (EMConversation) cle.getItem((int) id);
        // 删除和某个user会话，如果需要保留聊天记录，传false
        EMClient.getInstance().chatManager().deleteConversation(msg.getUserName(), true);
        // 刷新listView
        cle.refAll(list);
        // 在本次事件内 消化掉手势
        return false;
    }

    //--------------------------------------------------------------
    // listView的 item 点击事件
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent it = new Intent(getActivity(), PrivateMessageActivity.class);
//        // 获取点击事件的item内容数据
//        EMConversation emc = (EMConversation) cle.getItem(position);
//        if (emc.getType() == EMConversation.EMConversationType.GroupChat) {
//            // 把需要传递到下一个页面的数据 put到 intent里面
//            it.putExtra("groupId", emc.getUserName());
//        } else {
//            // 把需要传递到下一个页面的数据 put到 intent里面
//            it.putExtra("username", emc.getUserName());
//        }
//        startActivity(it);
//    }

    @Override
    public void onClick(int id) {
        ((BaseActivity) getActivity()).intentPrivateMessage(list.get(id).getUserName());
    }


    // -------------在PopulationActivity类中的 onMessageReceived()方法中-----------
    public void notifyList() {
        // 调用加载数据方法
        initDate();
        if (cle != null) {
            // 刷新listView
            cle.refAll(list);
        }
    }

    //--------------------------------------------------------------
    // 给list集合排序的方法 (在个人聊天中标题栏显示发送消息的人或群)
    private void sort() {
        // 集合排序 的 规则 接口
        Comparator comp = new Comparator<EMConversation>() {

            @Override
            public int compare(EMConversation o1, EMConversation o2) {

                // 判不判断交换位置要根据 1 和 -1 来的
                if (o1.getLastMessage().getMsgTime() < o2.getLastMessage().getMsgTime()) {
                    return 1;
                } else if (o1.getLastMessage().getMsgTime() == o2.getLastMessage().getMsgTime()) {
                    return 0;
                } else if (o1.getLastMessage().getMsgTime() > o2.getLastMessage().getMsgTime()) {
                    return -1;
                }
                return 0;
            }

        };
        // 主要的（使用这个规格来排序）
        Collections.sort(list, comp);
    }
}
