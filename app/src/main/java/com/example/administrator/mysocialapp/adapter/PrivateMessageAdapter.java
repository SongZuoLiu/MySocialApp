package com.example.administrator.mysocialapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import static com.example.administrator.mysocialapp.R.id.iv_mes_leftSendPicture;

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

    // TODO 暂时固定为自己的帐号，需写成 用户类 来存放
    private static final String MYUSER = "Song";

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
    public View getView(int position, View convertView, ViewGroup parent) {

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
        setViewContent(viewHolder, (EMMessage) getItem(position));
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
        // 把View return回去
        return convertView;
    }


    // ------------------------------------------------------------
    // 给控件设置数据的方法 (iewHolder控件对象，EMMessage消息对象)
    private void setViewContent(ViewHolder viewHolder, EMMessage emMessage) {
        // 设置控件的可见状态
        viewHolder.timeLay.setVisibility(View.VISIBLE);
        // 实例化时间格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm");
        // 获取消息的时间 并 格式化 然后给控件set上
        viewHolder.time.setText(dateFormat.format(emMessage.getMsgTime()));

        EMMessage.Type type = emMessage.getType();
        switch (type) {

            case TXT:
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
                    viewHolder.rightName.setText(emMessage.getUserName());
                    // 或 viewHolder.rightName.setText("我");
                    // 获取消息对象中的消息体 并强转成 文本消息体
                    EMTextMessageBody txt = (EMTextMessageBody) emMessage.getBody();
                    viewHolder.rightContent.setText(txt.getMessage());
                }
                break;
            case IMAGE:
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
                    viewHolder.rightName.setText(emMessage.getUserName());
                    Glide.with(context).load(localUrl).override(300, 200).
                            into(viewHolder.iv_mes_rightSendPicture);
                }
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

}
