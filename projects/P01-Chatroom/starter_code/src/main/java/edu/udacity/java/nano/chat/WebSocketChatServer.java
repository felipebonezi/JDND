package edu.udacity.java.nano.chat;

import br.com.felipebonezi.chatroom.models.EnterMessage;
import br.com.felipebonezi.chatroom.models.ChatMessage;
import br.com.felipebonezi.chatroom.models.LeaveMessage;
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
    private static final Map<String, Session> onlineSessions = new ConcurrentHashMap<>();

    private void sendMessageToAll(Message msg) {
        onlineSessions.forEach((key, session) -> this.sendMessage(session, msg));
    }

    private void sendMessage(Session session, Message msg) {
        if (msg instanceof EnterMessage) {
            EnterMessage enterMsg = (EnterMessage) msg;
            enterMsg.setOnlineCount(onlineSessions.size());
        }

        RemoteEndpoint.Async asyncRemote = session.getAsyncRemote();
        asyncRemote.sendText(JSON.toJSONString(msg), sendResult -> {
            if (sendResult.isOK())
                return;

            // Remove the session.
            onlineSessions.remove(session.getId());
        });
    }

    /**
     * Open connection, 1) add session, 2) add user.
     */
    @OnOpen
    public void onOpen(Session session) {
        onlineSessions.put(session.getId(), session);
        this.sendMessageToAll(new EnterMessage());
    }

    /**
     * Send message, 1) get username and session, 2) send message to all.
     */
    @OnMessage
    public void onMessage(Session session, String jsonStr) {
        if (!onlineSessions.containsKey(session.getId()))
            return;

        ChatMessage message = JSON.parseObject(jsonStr, ChatMessage.class);
        this.sendMessageToAll(message);
    }

    /**
     * Close connection, 1) remove session, 2) update user.
     */
    @OnClose
    public void onClose(Session session) {
        onlineSessions.remove(session.getId());
        this.sendMessageToAll(new LeaveMessage());
    }

    /**
     * Print exception.
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
        ChatMessage msg = new ChatMessage();
        msg.setUsername("system");
        msg.setMsg("Sorry, we cannot process your request because an error was throw.");
        this.sendMessage(session, msg);
    }

}
