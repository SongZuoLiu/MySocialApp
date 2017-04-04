package com.example.administrator.mysocialapp.act;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2017/4/1.
 */

public class BaseActivity extends AppCompatActivity {

    public void intentMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void intentPrivateMessage(String userName) {
        Intent intent = new Intent(this, PrivateMessageActivity.class);
        intent.putExtra("userName", userName);
        startActivity(intent);
    }
}
