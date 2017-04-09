package com.example.administrator.mysocialapp.act;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.mysocialapp.R;
import com.example.administrator.mysocialapp.adapter.AddFriendAdapter;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.name;
import static android.R.id.message;

public class AddFriendActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, AdapterView.OnItemLongClickListener {
    private EditText hao, reason;
    private Button add;
    private ListView listView;
    private String strmess, strname;
    private List<String> list;
    private List<String> newlist = new ArrayList<>();
    private Handler hander;
    AddFriendAdapter afa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        init();

        hao.setVisibility(View.GONE);
        reason.setVisibility(View.GONE);
        getFriend();
        //注册好友状态监听
        EMClient.getInstance().contactManager().setContactListener(getEMContactListener());


        hander = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    listView.setAdapter(new AddFriendAdapter(AddFriendActivity.this, newlist));
                }
            }
        };
        listView.setOnItemLongClickListener(this);

    }

    private void init() {
        hao = (EditText) findViewById(R.id.et_hao);
        reason = (EditText) findViewById(R.id.et_reason);
        add = (Button) findViewById(R.id.btn_add);
        listView = (ListView) findViewById(R.id.lv_addLv);
        add.setOnClickListener(this);
        add.setOnLongClickListener(this);

        afa = new AddFriendAdapter(this, newlist);
        listView.setAdapter(afa);
    }

    /**
     * 添加好友
     */
    private void addFriendOne() {
        try {
            strmess = hao.getText().toString();
            strname = reason.getText().toString();
            //同意好友请求
            EMClient.getInstance().contactManager().acceptInvitation(strname);
            //添加好友 参数为要添加的好友的username和添加理由
            EMClient.getInstance().contactManager().addContact(strname, strmess);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        try {
            strname.equals("");
        } catch (Exception e) {
            Toast.makeText(AddFriendActivity.this, "用户名为空", Toast.LENGTH_SHORT).show();
        }
        addFriendOne();
        UpDataFriend();
    }

    /**
     * 添加好友并刷新
     */
    private void UpDataFriend() {
        newlist.add(strname);
        AddFriendActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                afa.upData(newlist);
            }
        });
    }

    /**
     * 获取好友列表
     */
    private void getFriend() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //获取好友列表 获取好友的 username list，开发者需要根据 username 去自己服务器获取好友的详情。
                    list = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    if (list != null) {
                        newlist.addAll(list);
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                hander.sendEmptyMessage(1);
            }
        }).start();
    }


    /**
     * 好友状态监听
     *
     * @return
     */
    private EMContactListener getEMContactListener() {
        EMContactListener emContactListener = new EMContactListener() {
            @Override
            public void onContactAdded(String s) {
                AddFriendActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hao.setVisibility(View.GONE);
                        reason.setVisibility(View.GONE);
                    }
                });

            }

            @Override
            public void onContactDeleted(String s) {

            }

            @Override
            public void onContactInvited(String s, String s1) {

            }

            @Override
            public void onContactAgreed(String s) {

            }

            @Override
            public void onContactRefused(String s) {

            }
        };
        return emContactListener;
    }

    //------------------------
    @Override
    public boolean onLongClick(View v) {
        hao.setVisibility(View.VISIBLE);
        reason.setVisibility(View.VISIBLE);
        return true;
    }

    //删除好友
    private void delete(int i) {
        try {
            EMClient.getInstance().contactManager().deleteContact(newlist.get(i));
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder a = new AlertDialog.Builder(this);
        a.setTitle("是否删除好友");
        a.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete(position);
                String remove = newlist.remove(position);
            }
        });
        a.setNegativeButton("取消", null);
        a.show();
        return false;
    }

}
