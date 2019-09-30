package edu.udacity.java.nano.model;

public class LeaveMessage extends EnterMessage {

    @Override
    public Type getType() {
        return Type.LEAVE;
    }

}
