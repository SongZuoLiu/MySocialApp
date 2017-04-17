package com.example.administrator.mysocialapp.act;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.administrator.mysocialapp.R;

//图片点击一下就放大到全屏
//(这个 与 个人聊天适配器 相调用)
public class PictureBigActivity extends AppCompatActivity {
    private ImageView im_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_big);
        im_view = (ImageView) findViewById(R.id.im_view);
        Intent intent = getIntent();
        String ss = intent.getStringExtra("ss");
        Glide.with(this).load(ss).override(300, 200).
                into(im_view);

    }
}
