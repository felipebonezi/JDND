package edu.udacity.java.nano.chat;

/**
 * WebSocket message model
 */
public abstract class Message {

    public enum Type {
        ENTER,
        SPEAK,
        LEAVE
    }

    public Message() {
    }

    public abstract Type getType();

}
