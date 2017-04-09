package com.example.administrator.mysocialapp.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.administrator.mysocialapp.R;
import com.example.administrator.mysocialapp.act.LoginActivity;

/**
 * 设置 退出页面
 */
public class SetFragment extends Fragment implements View.OnClickListener {

    private View view;
    private Button person, returnLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.activity_set_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        person = (Button) view.findViewById(R.id.btn_person);
        returnLogin = (Button) view.findViewById(R.id.btn_returnLogin);

        person.setOnClickListener(this);
        returnLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_person:
                break;
            case R.id.btn_returnLogin:
                AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                builder.setTitle("应用提示");
                builder.setMessage("确定要退出吗?");
                //①
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                //②
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    }
                });
                builder.create().show();
                break;
        }
    }
}
