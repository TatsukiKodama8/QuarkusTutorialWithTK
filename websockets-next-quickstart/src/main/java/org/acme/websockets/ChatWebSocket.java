package org.acme.websockets;

import io.quarkus.websockets.next.OnClose;
import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.OnTextMessage;
import io.quarkus.websockets.next.WebSocket;
import io.quarkus.websockets.next.WebSocketConnection;
import jakarta.inject.Inject;

// Declares the web socket endpoint and configure the path. Note that the path can contain a path parameter: username.
@WebSocket(path = "/chat/{username}")  
public class ChatWebSocket {

    // Declare the type of messages that can be sent and received
    public enum MessageType {USER_JOINED, USER_LEFT, CHAT_MESSAGE}
    public record ChatMessage(MessageType type, String from, String message) {
    }

    // A session scoped bean that represents the connection to the client. It allows sending messages programmatically and retrieve the path parameters.
    @Inject
    WebSocketConnection connection;  

    // This method is called when a new client connects. The broadcast = true attribute indicates that the returned message should be sent to all connected clients.
    @OnOpen(broadcast = true) // クライアントが接続したときに呼ばれるメソッド。bloadcast = trueで全クライアントに送信される
    public ChatMessage onOpen() {
        return new ChatMessage(MessageType.USER_JOINED, connection.pathParam("username"), null);
    }

    // This method is called when a client disconnects. The method uses the WebSocketConnection to broadcast a message to all remaining connected clients.
    @OnClose                    
    public void onClose() {
        ChatMessage departure = new ChatMessage(MessageType.USER_LEFT, connection.pathParam("username"), null);
        connection.broadcast().sendTextAndAwait(departure);
    }

    // This method is called when a client sends a message. The broadcast = true attribute indicates that the returned message should be sent to all connected clients. Here, we just returns the received (text) message.
    @OnTextMessage(broadcast = true)  
    public ChatMessage onMessage(ChatMessage message) {
        return message;
    }

}