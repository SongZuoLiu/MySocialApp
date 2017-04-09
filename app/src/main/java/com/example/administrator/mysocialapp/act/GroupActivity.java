package com.example.administrator.mysocialapp.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.administrator.mysocialapp.R;
import com.example.administrator.mysocialapp.adapter.GroupMembersAdapter;
import com.hyphenate.chat.EMClient;

/**
 * 群信息页面(邀请成员加入)
 */
public class GroupActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editText;
    private Button button;
    private String groupId;
    private ListView memberLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        // 接收跳转所携带的数据（或 接收Intent传来的值）
        groupId = getIntent().getStringExtra("groupId");
        init();
        initListView();
    }

    private void init() {
        editText = (EditText) findViewById(R.id.ed_sid);
        button = (Button) findViewById(R.id.btn_find);
        memberLv = (ListView) findViewById(R.id.lv_listV);
        button.setOnClickListener(this);
    }

    // ---------------------------------------------------------
    private void initListView() {
        memberLv.setAdapter(new GroupMembersAdapter(this, groupId));
    }

    // --------------------------------------------------------
    @Override
    public void onClick(View v) {
        // 获取输入框内容 并以,分隔成字符串数组
        String str = editText.getText().toString();
        final String[] newmembers = str.split(",");
        // 启动线程
        new Thread(new Runnable() {

            public void run() {
                try {
                    // 邀请其他人入群（或 群主加人调用此方法）
                    EMClient.getInstance().groupManager().addUsersToGroup(groupId, newmembers);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
