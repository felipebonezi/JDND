package edu.udacity.java.nano.chat;

/**
 * WebSocket message model
 */
public class Message {

    public enum Type {
        ENTER,
        SPEAK,
        LEAVE
    }

    private int onlineCount;
    private String username;
    private String msg;
    private Type type;

    private Message() {
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public String getUsername() {
        return username;
    }

    public String getMsg() {
        return msg;
    }

    public Type getType() {
        return type;
    }

    public static class Builder {

        private int onlineCount;
        private String username;
        private String msg;
        private Type type;

        public Builder() {}

        public Message build() {
            Message msg = new Message();
            msg.onlineCount = this.onlineCount;
            msg.username = this.username;
            msg.msg = this.msg;
            msg.type = this.type;
            return msg;
        }

        public Builder setOnlineCount(int onlineCount) {
            this.onlineCount = onlineCount;
            return this;
        }

        public Builder setMsg(String msg) {
            this.msg = msg;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setType(Type type) {
            this.type = type;
            return this;
        }

    }

}
