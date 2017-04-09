package com.example.administrator.mysocialapp.act;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.mysocialapp.R;
import com.example.administrator.mysocialapp.utils.SPUtils;
import com.example.administrator.mysocialapp.view.CustonDialog;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import static android.text.TextUtils.isEmpty;

/**
 * 登录页
 * <p>
 * 第一种方法要继承并实现BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener
 */
public class LoginActivity extends AppCompatActivity {
    //---------------------第一种方法---------------------------------------
//    public static final String TAG = "LoginActivity";
//    private EditText name, password;
//    private Button login, register;
//    private CheckBox mPasswordCB;  //小眼睛
//    CustonDialog cd; //圆形进度加载
//    String username;
//    String passwords;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//        init();
//    }
//
//    private void init() {
//        name = (EditText) findViewById(R.id.userName_edt);
//        password = (EditText) findViewById(R.id.password_edt);
//        login = (Button) findViewById(R.id.btn_b_login);
//        register = (Button) findViewById(R.id.btn_b_register);
//        mPasswordCB = (CheckBox) findViewById(R.id.chat_login_password_checkbox);
//
//        login.setOnClickListener(this);
//        register.setOnClickListener(this);
//
//        mPasswordCB.setOnCheckedChangeListener(this);
//
//        // name.setText(SPUtils.getLastLoginUserName(this));
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btn_b_login:
//                login(); //开始登陆(下方有它的提取方法)
//                break;
//            case R.id.btn_b_register:
//                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//                startActivityForResult(intent, 1);
//                break;
//        }
//    }
//
//    //------------------------小眼睛的可见与不可见--------------------------------------
//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        if (isChecked) {
//            mPasswordCB.setChecked(true);
//            //动态设置密码是否可见
//            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//        } else {
//            mPasswordCB.setChecked(false);
//            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
//        }
//    }
//
//    //--------------------------------------------------------------
//    private void login() {
//        //开始登陆并判断
//        username = name.getText().toString();
//        passwords = password.getText().toString();
//        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(passwords)) {
//            Log.d(TAG, "帐号或密码不能为空！");
//            return;
//        }
//        /**
//         * 进度提示框:
//         ①第一种方法是直接写
//         ProgressDialog progressDialog = new ProgressDialog(this);
//         progressDialog.setIndeterminate(false);
//         progressDialog.setMessage("数据加载中...");
//         progressDialog.cancel();
//         */
//        //②第二种方法是写一个类继承ProgressDialog
//        cd = new CustonDialog(this, R.style.CustomDialog);
//        cd.show();
//        //环信中的登录服务器判断
//        EMClient.getInstance().login(username, passwords, new EMCallBack() {//回调
//            @Override
//            public void onSuccess() {
//                EMClient.getInstance().groupManager().loadAllGroups();
//                EMClient.getInstance().chatManager().loadAllConversations();
//
//                Log.d(TAG, "登录聊天服务器成功！");
//                //SPUtils.setLastLoginUserName(LoginActivity.this, username);
//                intentMain();
//                cd.cancel();
//            }
//
//            @Override
//            public void onProgress(int progress, String status) {
//
//            }
//
//            @Override
//            public void onError(int code, String message) {
//                Log.d(TAG, "登录聊天服务器失败！" + code + "  " + message);
//                cd.cancel();
//            }
//        });
//    }
//---------------------第二种方法---------------------------------------
    private String name, passwords;
    private CheckBox checkBox;
    private SharedPreferences sp;
    private boolean login = false;
    private EditText username, password;
    private CustonDialog proDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //初始化控件
        init();
        //判断之前是否登陆过，并如果用户名改变，则清空密码
        logBefore();
        //密码显示与隐藏监听
        checkBox();

    }

    public void init() {
        username = (EditText) findViewById(R.id.userName_edt);
        password = (EditText) findViewById(R.id.password_edt);
        checkBox = (CheckBox) findViewById(R.id.chat_login_password_checkbox);
        //初始化sp数据库
        sp = this.getSharedPreferences("passwordsave", MODE_PRIVATE);
        //光标位置
        username.setSelection(username.getText().toString().length());
    }

    private void logBefore() {
        // 如果登录成功过，直接进入主页面
        if (EMClient.getInstance().isLoggedInBefore()) {
            // ** 免登陆情况 加载所有本地群和会话
            //加上的话保证进了主页面会话和群组都已经load完毕
            EMClient.getInstance().groupManager().loadAllGroups();
            EMClient.getInstance().chatManager().loadAllConversations();
            login = true;
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            return;
        }
        // 如果用户名改变，清空密码
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password.setText(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    //登陆
    public void login(View view) {
        if (!isNetWorkConnected(this)) {
            Toast.makeText(this, "网络不可用", Toast.LENGTH_SHORT).show();
            return;
        }
        //获取输入的数据
        name = username.getText().toString();
        passwords = password.getText().toString();
        //判断密码正确错误
        if (isEmpty(name)) {
            Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isEmpty(passwords)) {
            Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();

            return;
        }
        //进度条显示
        proDialog = new CustonDialog(this, R.style.CustomDialog);
        proDialog.show();

        // 调用sdk登陆方法登陆聊天服务器
        EMClient.getInstance().login(name, passwords, new EMCallBack() {
            @Override
            public void onSuccess() {
                // 进入主页面
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                //关闭进度
                proDialog.cancel();
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(final int code, final String message) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        proDialog.cancel();
                        Toast.makeText(getApplicationContext(), "登陆失败,密码或账号错误", Toast.LENGTH_SHORT).show();
                    }
                });
//                startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                finish();
            }
        });
    }

    //注册
    public void register(View view) {
        startActivityForResult(new Intent(this, RegisterActivity.class), 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (login) {
            return;
        }
    }

    //检测网络是否可用
    public static boolean isNetWorkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable() && mNetworkInfo.isConnected();
            }
        }

        return false;
    }

    private void checkBox() {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //如果选中，显示密码
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //否则隐藏密码
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }
}
