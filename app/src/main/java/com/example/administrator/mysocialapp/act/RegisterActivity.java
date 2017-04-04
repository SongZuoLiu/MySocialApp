package com.example.administrator.mysocialapp.act;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.mysocialapp.R;
import com.example.administrator.mysocialapp.utils.CodeUtils;
import com.hyphenate.chat.EMClient;

/**
 * 注册页
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private EditText userName, passwords, password, mCodeET;
    private Button button;
    private ImageView mCodeIV;
    private String currCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        userName = (EditText) findViewById(R.id.register_username);
        passwords = (EditText) findViewById(R.id.register_passwords);
        password = (EditText) findViewById(R.id.register_password);
        mCodeET = (EditText) findViewById(R.id.chat_register_code);
        mCodeIV = (ImageView) findViewById(R.id.chat_register_password_code);
        button = (Button) findViewById(R.id.register_button);
        button.setOnClickListener(this);

        mCodeIV.setImageBitmap(CodeUtils.getInstance().createBitmap());
        currCode = CodeUtils.getInstance().getCode();
        //验证码的监听
        mCodeIV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mCodeIV.setImageBitmap(CodeUtils.getInstance()
                        .createBitmap());
                currCode = CodeUtils.getInstance().getCode();
                Log.i("TAG", "currentCode==>" + currCode);
            }
        });
    }

    @Override
    public void onClick(View v) {
        final String strName = userName.getText().toString();
        final String strPassw = passwords.getText().toString();
        final String strPass = password.getText().toString();
        final String code = mCodeET.getText().toString().trim();

        int i = TestusernameAndpassword(strName, strPassw, strPass, code);

        switch (i) {
            case 0:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("注册");
                builder.setMessage("确认信息无误");
                //①
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
//                        Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                    }
                });
                //②
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
//                                    Thread.sleep(3000);
                                    EMClient.getInstance().createAccount(strName, strPass);
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
//                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.create().show();
                break;
            //--------------------------------------------------------------
            case 1:
                Toast.makeText(this, "账号不能为空", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(this, "密码输入不一致", Toast.LENGTH_SHORT).show();
                break;
            case 5:
                Toast.makeText(this, "用户名太短", Toast.LENGTH_SHORT).show();
                break;
            case 6:
                Toast.makeText(this, "注意密码长度", Toast.LENGTH_SHORT).show();
                break;
            case 7:
                Toast.makeText(getApplicationContext(), "请输入验证码",
                        Toast.LENGTH_SHORT).show();
                break;
            case 8:
                Toast.makeText(getApplicationContext(), "验证码输入不正确",
                        Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    //--------------------------------------------------------------
    //检验注册时用户名和密码
    private int TestusernameAndpassword(String username, String passwords, String password, String code) {
        if (TextUtils.isEmpty(username)) {
            return 1;
        }
        if (TextUtils.isEmpty(passwords)) {
            return 2;
        }
        if (TextUtils.isEmpty(password)) {
            return 3;
        }
        if (!passwords.equals(password)) {
            return 4;
        }
        if (username.length() < 5) {
            Toast.makeText(this, "账号长度不能小于5位", Toast.LENGTH_SHORT).show();
            return 5;
        }
        if (passwords.length() < 6 || passwords.length() > 18) {
            return 6;
        }
        if (code.equals("")) {
            return 7;
        } else if (!code.equals(currCode.toLowerCase())) {
            return 8;
        }
        return 0;
    }
}
