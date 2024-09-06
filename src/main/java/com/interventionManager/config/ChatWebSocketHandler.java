package com.interventionManager.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interventionManager.entities.Chat;
import com.interventionManager.services.chat.ChatServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {
    private final ChatServiceImpl chatMessageService;
    private final List<WebSocketSession> webSocketSessions=new ArrayList<>();
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        webSocketSessions.add(session);
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Chat chatMessage = objectMapper.readValue(message.getPayload(), Chat.class);
        chatMessageService.saveMessage(chatMessage);
        for(WebSocketSession webSocketSession : webSocketSessions){
            //webSocketSession.sendMessage(message);
            if (webSocketSession.isOpen()) {
                try {
                    webSocketSession.sendMessage(message);
                } catch (Exception e) {
                    // Log the error and close the session if an exception occurs
                    webSocketSession.close(CloseStatus.SERVER_ERROR);
                }
            }
        }
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        webSocketSessions.remove(session);
    }

}