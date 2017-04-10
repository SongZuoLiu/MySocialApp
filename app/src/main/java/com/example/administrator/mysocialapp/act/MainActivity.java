package com.example.administrator.mysocialapp.act;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.mysocialapp.R;
import com.example.administrator.mysocialapp.fragment.LinkManFragment;
import com.example.administrator.mysocialapp.fragment.MessageFragment;
import com.example.administrator.mysocialapp.fragment.SetFragment;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.NetUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 主页 (消息列表页)
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, EMMessageListener, EMConnectionListener, ViewPager.OnPageChangeListener {
    private MessageFragment mf;
    private LinkManFragment lmf;
    private SetFragment sf;
    FragmentManager fm;
    private ImageView tv_message, tv_linkman, tv_set;
    private TextView tv_textView;
    private ViewPager viewpager;
    private List<Fragment> list = new ArrayList<>();
    private HashMap<String, String> textMap = new HashMap<>();
    private String str;
    private View ll_lineL;
    ScaleAnimation sa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        EMClient.getInstance().addConnectionListener(this);// 注册消息监听
        initFragment();
        testScaleBy();
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
        ll_lineL = findViewById(R.id.ll_lineL);
        viewpager = (ViewPager) findViewById(R.id.viewPager_pager);
        tv_message = (ImageView) findViewById(R.id.tv_message);
        tv_linkman = (ImageView) findViewById(R.id.tv_linkman);
        tv_set = (ImageView) findViewById(R.id.tv_set);
        tv_message.setOnClickListener(this);
        tv_linkman.setOnClickListener(this);
        tv_set.setOnClickListener(this);

        tv_textView = (TextView) findViewById(R.id.tv_textView);//链接已断开的按钮

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
        viewpager.setOnPageChangeListener(this);
    }

    //--------------------------------------------------------------
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_message:
                viewpager.setCurrentItem(0);
                NotificationsBackGroud(viewpager.getCurrentItem());
                tv_message.setAnimation(sa);
                tv_message.startAnimation(sa);
                break;
            case R.id.tv_linkman:
                viewpager.setCurrentItem(1);
                NotificationsBackGroud(viewpager.getCurrentItem());
                tv_linkman.setAnimation(sa);
                tv_linkman.startAnimation(sa);
                break;
            case R.id.tv_set:
                viewpager.setCurrentItem(2);
                NotificationsBackGroud(viewpager.getCurrentItem());
                tv_set.setAnimation(sa);
                tv_set.startAnimation(sa);
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

    //----------------------实现OnPageChangeListener的接口----------------------------------------
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        NotificationsBackGroud(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //----通知栏背景色---
    private void NotificationsBackGroud(int i) {
        if (i == 0) {
            tv_message.setBackgroundResource(R.color.colorAccent);
            tv_linkman.setBackgroundResource(R.color.colorPrimary);
            tv_set.setBackgroundResource(R.color.colorPrimary);
            tv_message.setAnimation(sa);
            tv_message.startAnimation(sa);
        }
        if (i == 1) {
            tv_message.setBackgroundResource(R.color.colorPrimary);
            tv_linkman.setBackgroundResource(R.color.colorAccent);
            tv_set.setBackgroundResource(R.color.colorPrimary);
            tv_linkman.setAnimation(sa);
            tv_linkman.startAnimation(sa);
        }
        if (i == 2) {
            tv_message.setBackgroundResource(R.color.colorPrimary);
            tv_linkman.setBackgroundResource(R.color.colorPrimary);
            tv_set.setBackgroundResource(R.color.colorAccent);
            tv_set.setAnimation(sa);
            tv_set.startAnimation(sa);
        }
    }

    //--------------------------------------------------------------
    @Override
    public void onConnected() {
        tv_textView.setVisibility(View.GONE);
    }

    @Override
    public void onDisconnected(final int error) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (error == EMError.USER_REMOVED) {
                    // 显示帐号已经被移除
                    toastShow(MainActivity.this, "帐号已经被移除");
                    tv_textView.setText("帐号已经被移除");
                    tv_textView.setVisibility(View.VISIBLE);
                } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    // 显示帐号在其他设备登录
                    toastShow(MainActivity.this, "帐号在其他设备登录");
                    tv_textView.setText("帐号在其他设备登录");
                    tv_textView.setVisibility(View.VISIBLE);
                } else {
                    if (NetUtils.hasNetwork(MainActivity.this)) {
                        //连接不到聊天服务器
                        toastShow(MainActivity.this, "连接不到聊天服务器");
                        tv_textView.setText("连接不到聊天服务器");
                        tv_textView.setVisibility(View.VISIBLE);
                    } else {
                        //当前网络不可用，请检查网络设置
                        toastShow(MainActivity.this, "当前网络不可用，请检查网络设置");
                        tv_textView.setText("当前网络不可用，请检查网络设置");
                        tv_textView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }


    //------------------------草稿-------------------------------------
    public void intentPrivateMessage(String userName) {
        Intent intent = new Intent(this, PrivateMessageActivity.class);
        intent.putExtra("userName", userName);
        if (!TextUtils.isEmpty(str))
            intent.putExtra("text", textMap.get(str));
        //startActivity(intent);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 101:
                str = data.getStringExtra("userName");
                textMap.put(str, data.getStringExtra("text"));
                try {
                    if (TextUtils.isEmpty(data.getStringExtra("text"))) {
                        textMap.remove(data.getStringExtra("userName"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mf.setChatText(textMap);
                break;
        }
    }

    //------------------------动画-------------------------------------
    private void testScaleBy() {
        sa = new ScaleAnimation(1.3f, 1.6f, 1.3f, 1.6f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
        sa.setDuration(2000);
        sa.setFillAfter(true);//设置 当动画结束后 保持动画结束时的状态
    }
}
