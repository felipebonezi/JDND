package edu.udacity.java.nano.chat;

/**
 * WebSocket message model
 */
public abstract class Message {

    public enum Type {
        SPEAK,
        ONLINE_USERS
    }

    public Message() {
    }

    public abstract Type getType();

}
