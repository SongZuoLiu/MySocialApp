package com.example.administrator.mysocialapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.mysocialapp.R;
import com.example.administrator.mysocialapp.act.PictureBigActivity;
import com.example.administrator.mysocialapp.act.VideoViewActivity;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVideoMessageBody;

import org.wlf.filedownloader.FileDownloader;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/4/4.
 * <p>
 * 个人聊天详情页的适配器
 */

public class PrivateMessageAdapter extends BaseAdapter {
    private Context context;
    private List<EMMessage> list;
    private String localUrl;
    private String pictureBigLocalUrl;
    EMVideoMessageBody em;

    // 构造方法 接收 上下文 和 数据源
    public PrivateMessageAdapter(Context context, List<EMMessage> list) {
        this.context = context;
        this.list = list;
    }

    // ------------------------------------------------------------
    // 实现BaseAdapter的接口
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // ------------------------------------------------------------
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        EMMessage msg = list.get(position);


//        //设置发送人 用户名
//        holder.name.setText(msg.getFrom());
        //获取消息类型
        EMMessage.Type typeMsg = msg.getType();

        ViewHolder viewHolder;
        // 判断view是否为空
        if (convertView == null) {
            // 实例化内部类
            viewHolder = new ViewHolder();
            // 加载布局给View (防止view重复)
            convertView = LayoutInflater.from(context).inflate(R.layout.item_private_message_adapter, parent, false);

            // 初始化控件
            viewHolder.initView(convertView);
            // 把内部类的对象set给View 当下次View不等于null时,
            // 可以直接取得已经初始化好的控件
            convertView.setTag(viewHolder);
        } else {
            // 获取之前set的初始化完成的控件对象(或 获取之前设置的(viewHolder)对象)
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 给控件设置数据 (调用下方的setViewContent方法)
        setViewContent(viewHolder, (EMMessage) getItem(position), position);
        //---------------------------------------------------------------------
        //别人所发图片的放大
        final Intent intent = new Intent(context, PictureBigActivity.class);
        viewHolder.iv_mes_leftSendPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("ss", pictureBigLocalUrl);
                context.startActivity(intent);
            }
        });
        //自己所发图片的放大
        viewHolder.iv_mes_rightSendPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("ss", localUrl);
                context.startActivity(intent);
            }
        });

        //------------------------对方接收视频---------------------------------------------
        viewHolder.iv_mes_leftSendPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (em.downloadStatus()) {
                    case DOWNLOADING:
                        break;
                    case SUCCESSED:
//                        Intent intent = new Intent(context, VideoViewActivity.class);
//                        intent.putExtra("rightPath", em.getLocalUrl());
//                        context.startActivity(intent);
                        break;
                    case FAILED:
                        break;
                    case PENDING:
                        HashMap<String, String> map = new HashMap<String, String>();
                        if (!TextUtils.isEmpty(em.getSecret())) {
                            map.put("share-secret", em.getSecret());
                        }
                        TestOtherSendVideo(map, position);
                        break;
                    default:
                        break;
                }
            }
        });
        //------自己所发视频----
        viewHolder.iv_mes_rightSendPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoViewActivity.class);
                EMVideoMessageBody body = (EMVideoMessageBody) list.get(position).getBody();
                intent.putExtra("rightPath", body.getLocalUrl());
                context.startActivity(intent);
            }
        });
        // 把View return回去
        return convertView;
    }

    private void TestOtherSendVideo(HashMap<String, String> map, int i) {
        final EMVideoMessageBody body = (EMVideoMessageBody) list.get(i).getBody();
        final String vs = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/" + System.currentTimeMillis() + ".mp4";

        EMClient.getInstance().chatManager().downloadFile(
                body.getRemoteUrl()
                , vs
                , map
                , new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        Log.e("onSuccess", "onSuccess");

                        Intent intent = new Intent(context, VideoViewActivity.class);
                        intent.putExtra("leftPath", vs);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.e("onError", "onError =" + i + " " + s);
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                }
        );
    }


    // ------------------------------------------------------------
    // 给控件设置数据的方法 (iewHolder控件对象，EMMessage消息对象)
    private void setViewContent(ViewHolder viewHolder, EMMessage emMessage, final int position) {
        // 设置控件的可见状态
        viewHolder.timeLay.setVisibility(View.VISIBLE);
        // 实例化时间格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm");
        // 获取消息的时间 并 格式化 然后给控件set上
        viewHolder.time.setText(dateFormat.format(emMessage.getMsgTime()));

        EMMessage.Type type = emMessage.getType();
        switch (type) {
            case TXT:
                caseTXT(viewHolder, emMessage);
                break;
            case IMAGE:
                caseIMAGE(viewHolder, emMessage);
                break;
            case VIDEO:
                caseVIDEO(viewHolder, emMessage);
                break;
        }
    }

    // ------------------------------------------------------------
    // 存放控件对象的内部类
    class ViewHolder {
        private LinearLayout timeLay, leftLay, rightLay;
        private TextView time, leftName, leftContent, rightName, rightContent;
        private ImageView leftImg, rightImg, iv_mes_leftSendPicture, iv_mes_rightSendPicture;

        // 初始化控件的方法
        void initView(View view) {
            timeLay = (LinearLayout) view.findViewById(R.id.ll_mes_item_lay);
            leftLay = (LinearLayout) view.findViewById(R.id.ll_mes_item_left);
            rightLay = (LinearLayout) view.findViewById(R.id.ll_mes_right_item);
            time = (TextView) view.findViewById(R.id.tv_mes_item_textview);
            leftName = (TextView) view.findViewById(R.id.tv_mes_item_left_name);
            rightName = (TextView) view.findViewById(R.id.tv_mes_item_right_name);
            leftContent = (TextView) view.findViewById(R.id.tv_mes_item_left_content);
            rightContent = (TextView) view.findViewById(R.id.tv_mes_item_right_content);
            leftImg = (ImageView) view.findViewById(R.id.iv_mes_left_item);
            rightImg = (ImageView) view.findViewById(R.id.iv_mes_item_right);
            iv_mes_leftSendPicture = (ImageView) view.findViewById(R.id.iv_mes_leftSendPicture);
            iv_mes_rightSendPicture = (ImageView) view.findViewById(R.id.iv_mes_rightSendPicture);
        }
    }

    // ----------------------①判断文本--------------------------------------
    private void caseTXT(ViewHolder viewHolder, EMMessage emMessage) {
        // 判断消息的发送方是不是自己
        if (emMessage.getFrom().equals(emMessage.getUserName())) {

            viewHolder.rightLay.setVisibility(View.GONE);
            viewHolder.leftLay.setVisibility(View.VISIBLE);
            // 获取消息对象中的消息体 并强转成 文本消息体
            // 需加其他消息类型viewHolder.leftName.setText(emMessage.getUserName());
            viewHolder.leftName.setText(emMessage.getUserName());

            EMTextMessageBody txt = (EMTextMessageBody) emMessage.getBody();
            viewHolder.leftContent.setText(txt.getMessage());
        } else {
            viewHolder.rightLay.setVisibility(View.VISIBLE);// 左边可见
            viewHolder.leftLay.setVisibility(View.GONE);// 右边隐藏
            // 设置用户名和内容
            //viewHolder.rightName.setText(emMessage.getUserName());
            viewHolder.rightName.setText("我");
            // 获取消息对象中的消息体 并强转成 文本消息体
            EMTextMessageBody txt = (EMTextMessageBody) emMessage.getBody();
            viewHolder.rightContent.setText(txt.getMessage());
        }
    }

    // -----------------------②判断图片-------------------------------------
    private void caseIMAGE(ViewHolder viewHolder, EMMessage emMessage) {
        // 判断图片的发送方是不是自己
        if (emMessage.getFrom().equals(emMessage.getUserName())) {

            viewHolder.rightLay.setVisibility(View.GONE);
            viewHolder.leftLay.setVisibility(View.VISIBLE);
            viewHolder.leftContent.setVisibility(View.GONE);
            viewHolder.iv_mes_leftSendPicture.setVisibility(View.VISIBLE);
            // 获取消息对象中的消息体 并强转成 文本消息体
            // 需加其他消息类型viewHolder.leftName.setText(emMessage.getUserName());
            viewHolder.leftName.setText(emMessage.getUserName());
            EMImageMessageBody im = (EMImageMessageBody) emMessage.getBody();
            pictureBigLocalUrl = im.getThumbnailUrl();
            Glide.with(context).load(pictureBigLocalUrl).override(300, 200).
                    into(viewHolder.iv_mes_leftSendPicture);

        } else {
            EMImageMessageBody im = (EMImageMessageBody) emMessage.getBody();
            localUrl = im.getLocalUrl();
            viewHolder.rightLay.setVisibility(View.VISIBLE);// 左边可见
            viewHolder.leftLay.setVisibility(View.GONE);// 右边隐藏
            // 设置用户名和内容
            viewHolder.rightContent.setVisibility(View.GONE);
            viewHolder.iv_mes_rightSendPicture.setVisibility(View.VISIBLE);
            viewHolder.rightName.setText("我");
            Glide.with(context).load(localUrl).override(300, 200).
                    into(viewHolder.iv_mes_rightSendPicture);
        }
    }

    // -----------------------③判断视频-------------------------------------
    private void caseVIDEO(ViewHolder viewHolder, EMMessage emMessage) {
        // 判断视频的发送方是不是自己
        if (emMessage.getFrom().equals(emMessage.getUserName())) {

            viewHolder.rightLay.setVisibility(View.GONE);
            viewHolder.leftLay.setVisibility(View.VISIBLE);
            viewHolder.leftContent.setVisibility(View.GONE);
            viewHolder.iv_mes_leftSendPicture.setVisibility(View.VISIBLE);
            viewHolder.leftName.setText(emMessage.getUserName());
            em = (EMVideoMessageBody) emMessage.getBody();

            Glide.with(context)
                    .load(R.mipmap.ic_launcher)
                    .override(300, 200).
                    into(viewHolder.iv_mes_leftSendPicture);

        } else {
            final EMVideoMessageBody em = (EMVideoMessageBody) emMessage.getBody();
            viewHolder.rightLay.setVisibility(View.VISIBLE);// 左边可见
            viewHolder.leftLay.setVisibility(View.GONE);// 右边隐藏
            // 设置用户名和内容
            viewHolder.rightContent.setVisibility(View.GONE);
            viewHolder.iv_mes_rightSendPicture.setVisibility(View.VISIBLE);
            viewHolder.rightName.setText("我");
            Glide.with(context).load(em.getLocalUrl()).override(300, 200).
                    into(viewHolder.iv_mes_rightSendPicture);
        }
    }
}