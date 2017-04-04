package com.example.administrator.mysocialapp.act;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.mysocialapp.R;
import com.example.administrator.mysocialapp.fragment.LinkManFragment;
import com.example.administrator.mysocialapp.fragment.MessageFragment;
import com.example.administrator.mysocialapp.fragment.SetFragment;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页 (消息列表页)
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, EMMessageListener {
    private MessageFragment mf;
    private LinkManFragment lmf;
    private SetFragment sf;
    FragmentManager fm;
    private TextView tv_message, tv_linkman, tv_set;
    private ViewPager viewpager;
    private List<Fragment> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 注册消息监听
        EMClient.getInstance().chatManager().addMessageListener(this);

        init();
        initFragment();
    }

    //--------------------------------------------------------------
    //在活动准备好和用户进行交互的时候调用。此时活动一定位于返回栈的栈顶，并处于运行状态
    @Override
    protected void onResume() {
        super.onResume();
        mf.notifyList();
    }

    @Override
    protected void onDestroy() {  //在活动被销毁之前调用，之后活动状态变为销毁
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(this);
    }

    //--------------------------------------------------------------
    private void init() {
        viewpager = (ViewPager) findViewById(R.id.viewPager_pager);
        tv_message = (TextView) findViewById(R.id.tv_message);
        tv_linkman = (TextView) findViewById(R.id.tv_linkman);
        tv_set = (TextView) findViewById(R.id.tv_set);
        tv_message.setOnClickListener(this);
        tv_linkman.setOnClickListener(this);
        tv_set.setOnClickListener(this);

        mf = new MessageFragment();
        lmf = new LinkManFragment();
        sf = new SetFragment();
        list.add(mf);
        list.add(lmf);
        list.add(sf);
    }

    //--------------------------------------------------------------
    private void initFragment() {
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        FragmentPagerAdapter fpa = new FragmentPagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Override
            public int getCount() {
                return list.size();
            }
        };
        viewpager.setAdapter(fpa);
        viewpager.setCurrentItem(0);
    }

    //--------------------------------------------------------------
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_message:
                viewpager.setCurrentItem(0);
                break;
            case R.id.tv_linkman:
                viewpager.setCurrentItem(1);
                break;
            case R.id.tv_set:
                viewpager.setCurrentItem(2);
                break;
        }
    }

    //----------------------(接收消息)----------------------------------------
    @Override
    public void onMessageReceived(List<EMMessage> list) {
        mf.notifyList();  //在MessageFragment中   刷新
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageReadAckReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageDeliveryAckReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }
}
