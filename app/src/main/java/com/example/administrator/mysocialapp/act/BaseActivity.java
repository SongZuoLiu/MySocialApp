package com.example.administrator.mysocialapp.act;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/4/1.
 */

public class BaseActivity extends AppCompatActivity {

    public void intentMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //-------------------------------------------------------------

    /**
     * Toast提示信息的方法
     */
    public static void toastShow(final Activity activity, final String message) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}