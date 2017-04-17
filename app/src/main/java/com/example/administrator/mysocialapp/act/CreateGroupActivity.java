package com.example.administrator.mysocialapp.act;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.administrator.mysocialapp.adapter.CreateGroupAdapter;
import com.example.administrator.mysocialapp.bean.CreatGroupBean;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建群组界面
 */
public class CreateGroupActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private EditText editText;
    private Button button;
    private ListView listView;
    private List<EMGroup> groupsList;
    CreateGroupAdapter createGroupAdapter;
    ArrayList<EMGroup> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        init();
        initListView();
    }

    private void init() {
        editText = (EditText) findViewById(R.id.et_zi);
        button = (Button) findViewById(R.id.btn_create);
        button.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.lView);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }

    // -------------------------------------------------------
    @Override
    public void onClick(View v) {
        // 获取创建群组所填的信息
        CreatGroupBean cgb = getData();
        try {
            // 创建群组
            EMClient.getInstance().groupManager().createGroup(cgb.getGroupName(), cgb.getDesc(), cgb.getAllMembers(),
                    cgb.getReason(), cgb.getOption());
            Toast.makeText(this, "创建成功!", Toast.LENGTH_SHORT).show();
            finish();
        } catch (HyphenateException e) {
            // 创建失败 会抛出异常 进行失败提示
            Toast.makeText(this, "创建失败!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    // ------------------------------------------------------------
    // (调用的是bean包里的CreatGroupBean)
    // 获取创建群组 所填的方法
    private CreatGroupBean getData() {
        // TODO 实现其他属性的交互 校验数据
        CreatGroupBean cgb = new CreatGroupBean();
        cgb.setGroupName(editText.getText().toString());
        cgb.setAllMembers(new String[]{});// 空字符串
        cgb.setDesc("");
        cgb.setReason("");
        // 设置群组类型
        EMGroupManager.EMGroupOptions option = new EMGroupManager.EMGroupOptions();
        option.maxUsers = 200;
        option.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;

        // option里的Groupstyle分别为（环信所支持四种群）：
        // EMGroupStylePrivateOnlyOwnerInvite——私有群，只有群主可以邀请人；
        // EMGroupStylePrivateMemberCanInvite——私有群，群成员也能邀请人进群；
        // EMGroupStylePublicJoinNeedApproval——公开群，加入此群除了群主邀请，只能通过申请加入此群；
        // EMGroupStylePublicOpenJoin ——公开群，任何人都能加入此群。
        cgb.setOption(option);
        return cgb;
    }


    // -------------------------------------------------------
    // 获取群组列表方法
    private void getGroupData() {
        try {
            // 从服务器获取 群组 列表
            // TODO 需要异步处理
            EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        // 从本地 数据库中获取 群组 列表
        groupsList = EMClient.getInstance().groupManager().getAllGroups();
    }

    private void initListView() {
        // 获取群组数据
        getGroupData();
        // 实例化listView适配器
        createGroupAdapter = new CreateGroupAdapter(CreateGroupActivity.this, groupsList);
        // 给listView设置 适配器
        listView.setAdapter(createGroupAdapter);
    }

    // -------------------------------------------------------
    // listView的item的点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 获取点击item的数据内容
        EMGroup emGroup = (EMGroup) createGroupAdapter.getItem(position);
        // 带数据的跳转页面
        intentData(PrivateMessageActivity.class, emGroup.getGroupId());
    }

    // ------------------无数据也可以不用写-------------------------------------
    // 无数据跳转
    public void intentData(Class<?> cls) {
        Intent intent = new Intent(CreateGroupActivity.this, cls);
        CreateGroupActivity.this.startActivity(intent);
    }

    // 携带String类型的 groupId的跳转
    private void intentData(Class<?> cls, String groupId) {
        Intent intent = new Intent(CreateGroupActivity.this, cls);
        intent.putExtra("groupId", groupId);
        CreateGroupActivity.this.startActivity(intent);
    }

    // -------------------------------------------------------
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this.getApplicationContext());
//        //AlertDialog.Builder builder = new AlertDialog.Builder(CreateGroupActivity.this);
//        builder.setTitle("确认");
//        builder.setMessage("是否确认解散该群？");
//        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//
//        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
        // 获取点击事件的item内容数据
        EMGroup msg = groupsList.get(position);
        try {
            //解散群组
            EMClient.getInstance().groupManager().destroyGroup(msg.getGroupId());
            Toast.makeText(CreateGroupActivity.this, "群组解散成功", Toast.LENGTH_SHORT).show();
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        // 刷新listView
        groupsList.remove(position);
        createGroupAdapter.refAlls(list);
        // 在本次事件内 消化掉手势
        return false;
    }

}
