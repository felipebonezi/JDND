package br.com.felipebonezi.chatroom.models;

import edu.udacity.java.nano.chat.Message;

public class EnterMessage extends Message {

    private int onlineCount;

    public EnterMessage() {
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }

    @Override
    public Message.Type getType() {
        return Message.Type.ENTER;
    }

}
