package com.example.administrator.mysocialapp.act;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.mysocialapp.R;


/**
 * 启动页
 */
public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout splash;
    private TextView number;
    AlphaAnimation animation;
    MyCount my;

//
//    Handler handler=new Handler(){
//        public void handlerMessage(Message m){
//
//        }
//    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE); //隐藏启动页的标题
        setContentView(R.layout.activity_splash);
        init();
        countDown();

//        Message msg=new Message();
//        handler.sendMessageDelayed(msg,INTENT_TIME);
    }

    private void init() {
        splash = (RelativeLayout) findViewById(R.id.activity_splash);
        number = (TextView) findViewById(R.id.tv_number);
        number.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        my.cancel();
        finish();
    }

    //-------------------设置的倒计时时间-------------------------------------------
    private void countDown() {
        animation = new AlphaAnimation(0.5f, 1.0f);  //背景透明度
        animation.setDuration(3000); //透明度时间
        my = new MyCount(4000, 1000);
        my.start();
        splash.setAnimation(animation);
    }

    //--------------------倒计时------------------------------------------
    public class MyCount extends CountDownTimer {

        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            number.setText("跳过" + millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish() {
            Intent it = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(it);
            finish();
        }
    }

    //-----------------------一般开屏页都要写---------------------------------------
    //后退键(避免按下返回键直接弹到桌面)（实现的效果是 按下返回键仍执行操作，跳转到下一个界面，不返回桌面）
    @Override
    public void onBackPressed() {
    }
}
