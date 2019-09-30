package edu.udacity.java.nano.model;

import edu.udacity.java.nano.chat.Message;

public class ChatMessage extends EnterMessage {

    private String username;
    private String msg;

    public ChatMessage() {
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
