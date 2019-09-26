package br.com.felipebonezi.chatroom.models;

import edu.udacity.java.nano.chat.Message;

public class SpeakMessage extends OnlineUserMessage {

    private String username;
    private String msg;

    public SpeakMessage() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public Message.Type getType() {
        return Message.Type.SPEAK;
    }

}
