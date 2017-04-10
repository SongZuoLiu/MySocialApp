package com.example.administrator.mysocialapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.administrator.mysocialapp.R;
import com.example.administrator.mysocialapp.adapter.PrivateImageSelectAdapter;
import com.example.administrator.mysocialapp.utils.FuilUtils;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Administrator on 2017/4/10.
 */

public class PrivateImageSelectFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private Button send;
    private ArrayList<String> list = new ArrayList<>();
    PrivateImageSelectAdapter privateImageSelectAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.private_fragment_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = FuilUtils.getAllImg(getActivity());
        initView(view);
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_imageselect_list);
        send = (Button) view.findViewById(R.id.fragment_imageselect_send_btn);

        privateImageSelectAdapter = new PrivateImageSelectAdapter(getActivity(), list);

        //线性布局管理器  设置 水平滚动
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(privateImageSelectAdapter);

        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_imageselect_send_btn:
                HashSet<String> checkList = privateImageSelectAdapter.getCheckList();
                for (String str :
                        checkList) {
                    Log.e("checkList", str);
                }
                break;
        }
    }
}
