package edu.udacity.java.nano.chat;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket Server
 *
 * @see ServerEndpoint WebSocket Client
 * @see Session   WebSocket Session
 */
@Component
@ServerEndpoint("/chat")
public class WebSocketChatServer {

    /**
     * All chat sessions.
     */
    private static final Map<String, Session> SESSIONS = new ConcurrentHashMap<>();

    private void sendMessageToAll(Message msg) {
        SESSIONS.forEach((key, session) -> this.sendMessage(session, msg));
    }

    private void sendMessage(Session session, Message msg) {
        RemoteEndpoint.Async asyncRemote = session.getAsyncRemote();
        asyncRemote.sendText(JSON.toJSONString(msg), sendResult -> {
            if (sendResult.isOK())
                return;

            // Remove the session.
            SESSIONS.remove(session.getId());
        });
    }

    /**
     * Open connection, 1) add session, 2) add user.
     */
    @OnOpen
    public void onOpen(Session session) {
        String username = session.getId();
        SESSIONS.put(username, session);
        Message msg = new Message.Builder()
                .setType(Message.Type.ENTER)
                .setUsername(username)
                .setOnlineCount(SESSIONS.size())
                .build();
        this.sendMessageToAll(msg);
    }

    /**
     * Send message, 1) get username and session, 2) send message to all.
     */
    @OnMessage
    public void onMessage(Session session, String jsonStr) {
        if (!SESSIONS.containsKey(session.getId()))
            return;

        Message msg = JSON.parseObject(jsonStr, Message.Builder.class)
                .setType(Message.Type.SPEAK)
                .setOnlineCount(SESSIONS.size())
                .build();
        this.sendMessageToAll(msg);
    }

    /**
     * Close connection, 1) remove session, 2) update user.
     */
    @OnClose
    public void onClose(Session session) {
        String username = session.getId();
        SESSIONS.remove(username);
        Message msg = new Message.Builder()
                .setType(Message.Type.LEAVE)
                .setUsername(username)
                .setOnlineCount(SESSIONS.size())
                .build();
        this.sendMessageToAll(msg);
    }

    /**
     * Print exception.
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
        Message msg = new Message.Builder()
                .setType(Message.Type.SPEAK)
                .setUsername("system")
                .setMsg("Sorry, we cannot process your request because an error was throw.")
                .setOnlineCount(SESSIONS.size())
                .build();
        this.sendMessage(session, msg);
    }

}
