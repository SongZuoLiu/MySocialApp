package com.example.administrator.mysocialapp.act;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.administrator.mysocialapp.R;

public class VideoViewActivity extends Activity {
    private VideoView videoView;
    private String leftPath, rightPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        getData();
        initView();
    }

    private void initView() {
        videoView = (VideoView) findViewById(R.id.videoView);
        if (TextUtils.isEmpty(rightPath)) {
            videoView.setVideoPath(leftPath);
        } else {
            videoView.setVideoPath(rightPath);
        }

        videoView.start();
        //下面这两个的效果是 播放视频时显示播放进度
        videoView.setMediaController(new MediaController(this));
        videoView.requestFocus();
    }

    public void getData() {
        leftPath = getIntent().getStringExtra("leftPath");//接收方
        rightPath = getIntent().getStringExtra("rightPath");//发送方
    }
}

