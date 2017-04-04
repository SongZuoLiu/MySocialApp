package com.example.administrator.mysocialapp.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.mysocialapp.R;
import com.example.administrator.mysocialapp.adapter.PrivateMessageAdapter;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人聊天详情页
 */
public class PrivateMessageActivity extends BaseActivity implements View.OnClickListener, EMMessageListener, EMCallBack {
    private TextView textView, titleName;
    private ListView msgShowList;
    private EditText textEdit;
    private Button sendBtn;
    private Button togroupBtn;
    private String userName;
    private String groupId;
    private List<EMMessage> messages=new ArrayList<>();
    private PrivateMessageAdapter privateMessageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_message);
        EMClient.getInstance().chatManager().addMessageListener(this);
        userName = getIntent().getStringExtra("userName");
        groupId = getIntent().getStringExtra("groupId");
        initView();
        setTitleName();
        initListView();
    }

    private void initView() {
        textEdit = (EditText) findViewById(R.id.private_message_editText);
        msgShowList = (ListView) findViewById(R.id.private_message_lv);
        sendBtn = (Button) findViewById(R.id.private_message_send_btn);

        msgShowList.setSelection(msgShowList.getBottom());// 实现滑动list的效果
        sendBtn.setOnClickListener(this);
        togroupBtn = (Button) findViewById(R.id.private_message_send_btn);
        togroupBtn.setOnClickListener(this);

        titleName = (TextView) findViewById(R.id.private_message_title_name);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.private_message_send_btn:
                String str = textEdit.getText().toString();
                try {
                    sendTxt(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                textEdit.setText("");
                break;
        }
    }

    //--------------------------------------------------------------
    // 发送消息的方法
    private void sendTxt(String str) {
        EMMessage message;
        if (TextUtils.isEmpty(userName)) {
            // 1.创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
            message = EMMessage.createTxtSendMessage(str, groupId);
        } else {
            message = EMMessage.createTxtSendMessage(str, userName);
        }

        // 2.如果是群聊，设置chattype，默认是单聊
        if (TextUtils.isEmpty(userName)) {
            message.setChatType(EMMessage.ChatType.GroupChat);
        }

        // 设置消息状态回调
        message.setMessageStatusCallback(this);
        // 3.发送消息
        EMClient.getInstance().chatManager().sendMessage(message);
        addMessageList(message);
    }

    private void addMessageList(EMMessage message) {
        messages.add(message);
        privateMessageAdapter.notifyDataSetChanged();
        // 设置被选的Item，liseview滚动到最后
        msgShowList.setSelection(msgShowList.getBottom());
    }

    //--------------------------------------------------------------
    //设置用户名
    private void setTitleName() {
        if (TextUtils.isEmpty(groupId)) {
            titleName.setText(userName);
            //sendBtn.setVisibility(View.GONE);
        } else {
            titleName.setText(groupId);
            sendBtn.setVisibility(View.VISIBLE);
        }
    }

    private void initListView() {
        initData();
        privateMessageAdapter = new PrivateMessageAdapter(this, messages);
        msgShowList.setAdapter(privateMessageAdapter);
    }


    // 获取聊天记录
    private void initData() {

        if (TextUtils.isEmpty(groupId)) {
            // 获取单个聊天
            EMConversation conversation = EMClient.getInstance()
                    .chatManager().getConversation(userName);

            // 获取此会话的所有消息
            messages = conversation.getAllMessages();
        } else {
            // 获取单个聊天
            EMConversation conversation = EMClient.getInstance().chatManager().getConversation(groupId);
            if (conversation != null) {
                // 获取此会话的所有消息
                messages = conversation.getAllMessages();
            } else {
                messages = new ArrayList<EMMessage>();
            }
        }
    }

    //-------------------(接收消息)-------------------------------
    //实现EMMessageListener接口
    //①收到消息
    @Override
    public void onMessageReceived(final List<EMMessage> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (EMMessage message : list) {
                    addMessageList(message);
                }
            }
        });
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {
        //收到透传消息
    }

    @Override
    public void onMessageReadAckReceived(List<EMMessage> list) {
        //收到已读回执
    }

    @Override
    public void onMessageDeliveryAckReceived(List<EMMessage> list) {
        //收到已送达回执
    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {
        //消息状态变动
    }

    //--------------------------------------------------------------
    //实现EMCallBack接口
    @Override
    public void onSuccess() { //控件隐藏

    }

    //一般要写一个红色叹号
    @Override
    public void onError(int i, String s) {

    }

    @Override
    public void onProgress(int i, String s) {

    }
}
