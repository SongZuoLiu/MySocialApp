package com.example.administrator.mysocialapp.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.administrator.mysocialapp.R;


/**
 * Created by Administrator on 2017/3/27.
 * <p>
 * 圆形进度加载
 */

public class CustonDialog extends ProgressDialog {


    public CustonDialog(Context context) {
        super(context);
    }

    //样式主题
    public CustonDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this.setIndeterminate(false)本类对象的使用，this可以省略
        setIndeterminate(false);
        setCanceledOnTouchOutside(false);

        setContentView(R.layout.login_dialog);

        WindowManager.LayoutParams arr = getWindow().getAttributes();

        arr.width = WindowManager.LayoutParams.WRAP_CONTENT;//宽
        arr.height = WindowManager.LayoutParams.WRAP_CONTENT;//高
        arr.alpha = 0.8f; //透明度

        getWindow().setAttributes(arr);

    }
}
