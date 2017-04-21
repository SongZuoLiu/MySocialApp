package com.example.administrator.mysocialapp.Manager;


import com.example.administrator.mysocialapp.callback.MessageListListener;

/**
 * 这页不用，已废弃
 */

public class MessageManager {
    private static MessageManager messageManager;

    private MessageListListener messageListListener;

    public MessageListListener getMessageListListener() {
        return messageListListener;
    }

    public void setMessageListListener(MessageListListener messageListListener) {
        this.messageListListener = messageListListener;
    }

    public static synchronized MessageManager getInsatance() {
        if (messageManager == null) {
            messageManager = new MessageManager();
        }
        return messageManager;
    }


}
