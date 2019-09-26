package br.com.felipebonezi.chatroom.models;

public class LeaveMessage extends EnterMessage {

    @Override
    public Type getType() {
        return Type.LEAVE;
    }

}
