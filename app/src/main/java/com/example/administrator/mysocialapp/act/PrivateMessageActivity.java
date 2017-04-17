package com.example.administrator.mysocialapp.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.mysocialapp.R;
import com.example.administrator.mysocialapp.adapter.PrivateMessageAdapter;
import com.example.administrator.mysocialapp.fragment.PrivateImageSelectFragment;
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
public class PrivateMessageActivity extends BaseActivity implements View.OnClickListener, EMMessageListener, EMCallBack, AdapterView.OnItemLongClickListener {
    private TextView titleName;
    private ListView msgShowList;
    private EditText textEdit;
    private ImageView imageView;
    private Button sendBtn, btn_pictures, btn_photo, btn_yuYin;
    private String userName, groupId;
    private List<EMMessage> messages = new ArrayList<>();
    private PrivateMessageAdapter privateMessageAdapter;
    EMConversation conversation;
    /************
     * 点击发送图片按钮所用的对象
     *********************/
    FragmentManager fragmentManager;
    PrivateImageSelectFragment privateImageSelectFragment;
    String text;
    FragmentTransaction transaction;

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
        InitFragment();

        actionBar();
    }

    //-------------------标题栏的返回键  选择框-------------------------------------------
    private void actionBar() {
        try {
            //getSupportActionBar().setHomeAsUpIndicator();//设置返回键的图片(很少用)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(userName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //选择框
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                intent.putExtra("text", text);
                intent.putExtra("userName", userName);
                setResult(RESULT_OK, intent);
                finish();
                return true;
            case R.id.test1:
                Toast.makeText(PrivateMessageActivity.this, "扫一扫", Toast.LENGTH_SHORT).show();
                break;
            case R.id.test2:
                Toast.makeText(PrivateMessageActivity.this, "收付款", Toast.LENGTH_SHORT).show();
                break;
            case R.id.test3:
                Toast.makeText(PrivateMessageActivity.this, "发起群聊", Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.privatemessageright, menu);
        return super.onCreateOptionsMenu(menu);
    }


    //--------------------------------------------------------------

    /************
     * 点击发送图片按钮所用的
     *************************/
    private void InitFragment() {
        fragmentManager = getSupportFragmentManager();
        privateImageSelectFragment = new PrivateImageSelectFragment();
    }

    /********************************************************/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(this);
    }

    @Override
    public void onBackPressed() {
        //text = editText.getText().toString();
        Intent intent = new Intent();
        intent.putExtra("text", text);
        intent.putExtra("userName", userName);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    private void initView() {
        textEdit = (EditText) findViewById(R.id.private_message_editText);
        msgShowList = (ListView) findViewById(R.id.private_message_lv);
        sendBtn = (Button) findViewById(R.id.private_message_send_btn);
        imageView = (ImageView) findViewById(R.id.private_message_title_right);
        imageView.setOnClickListener(this);
        //------------------------草稿-------------------------------------
        userName = getIntent().getStringExtra("userName");
        text = getIntent().getStringExtra("text");

        if (!TextUtils.isEmpty(text)) {
            textEdit.setText(text);
            textEdit.setSelection(textEdit.getText().length());
        }

        textEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                text = s.toString();
            }
        });
        //-------------------------------------------------------------
        btn_pictures = (Button) findViewById(R.id.btn_pictures);
        btn_photo = (Button) findViewById(R.id.btn_photo);
        btn_yuYin = (Button) findViewById(R.id.btn_yuYin);
        btn_pictures.setOnClickListener(this);
        btn_photo.setOnClickListener(this);
        btn_yuYin.setOnClickListener(this);

        msgShowList.setOnItemLongClickListener(this);
        msgShowList.setSelection(msgShowList.getBottom());// 实现滑动list的效果
        sendBtn.setOnClickListener(this);
        btn_photo = (Button) findViewById(R.id.private_message_send_btn);

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
                conversation.setExtField(""); //-------草稿--------
                privateMessageAdapter.notifyDataSetChanged();
                break;
            case R.id.private_message_title_right:
                Intent intent = new Intent(PrivateMessageActivity.this, GroupActivity.class);
                intent.putExtra("groupId", groupId);
                startActivity(intent);
                break;
            /************点击底部的发送图片按钮所进行的判断*********************/
            case R.id.btn_pictures:
                //判断底部fragment是否添加过  如果有则 删除fragment 反之 添加
                if (privateImageSelectFragment.isAdded()) {
                closeImgFragment();
            } else {
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.privateMessage_bottom_fragment_lay, privateImageSelectFragment);
                transaction.addToBackStack("message_bottom_fragment_lay");
                transaction.commit();
            }
            break;
        }
    }

    private void closeImgFragment() {
        transaction = fragmentManager.beginTransaction();
        transaction.remove(privateImageSelectFragment);
        transaction.commit();
        //从fragment的返回栈中移除fragment
        fragmentManager.popBackStackImmediate("privateMessage_bottom_fragment_lay", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
    //-------------------发送图片-------------------------------------------

    /**
     * @param imgPath     图片路径
     * @param isThumbnail 是否发送原图  true 原图  false 缩略图
     */
    public void sendImage(String imgPath, boolean isThumbnail) {
        EMMessage message = EMMessage.createImageSendMessage(imgPath, isThumbnail, userName);
        sendMessage(message);
    }

    private void sendMessage(EMMessage message) {
        //如果是群聊，设置chattype，默认是单聊
//                if (chatType == CHATTYPE_GROUP)
        message.setChatType(EMMessage.ChatType.Chat);
        message.setMessageStatusCallback(this);

        //发送消息
        EMClient.getInstance()
                .chatManager()
                .sendMessage(message);
        //图片发送之后 关闭图片选择fragment
        if (privateImageSelectFragment.isAdded()) {
            closeImgFragment();
        }
        messages.add(message);

        //调用刷新消息列表的方法
        privateMessageAdapter.notifyDataSetChanged();
    }


    //-----------------发送消息文本的方法---------------------------------------------
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
        text = "";
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
            conversation = EMClient.getInstance()
                    .chatManager().getConversation(userName);

            // 获取此会话的所有消息
            messages = conversation.getAllMessages();
        } else {
            // 获取单个聊天
            conversation = EMClient.getInstance().chatManager().getConversation(groupId);
            if (conversation != null) {
                // 获取此会话的所有消息
                messages = conversation.getAllMessages();
            } else {
                messages = new ArrayList<EMMessage>();
            }
        }
    }

    //--------------------------------------------------------------
    // listView的item 长按事件
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        // 获取点击事件的item内容数据
        EMMessage msg = (EMMessage) privateMessageAdapter.getItem((int) id);
        // 删除和某个user会话，如果需要保留聊天记录，传false
        conversation.removeMessage(msg.getMsgId());
        messages.remove((int) id);
        // 刷新listView
        privateMessageAdapter.notifyDataSetChanged();
        return false;
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
        Log.e("onSuccess", "onSuccess");
    }

    //一般要写一个红色叹号
    @Override
    public void onError(int i, String s) {
        Log.e("onError", "onError" + i + " " + s);
    }

    @Override
    public void onProgress(int i, String s) {

    }


}
