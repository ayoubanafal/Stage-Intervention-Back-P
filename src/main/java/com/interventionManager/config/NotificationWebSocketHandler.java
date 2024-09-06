package com.interventionManager.config;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NotificationWebSocketHandler  extends TextWebSocketHandler {
    //private final List<WebSocketSession> webSocketSessions = new ArrayList<>();
    private final Map<Long, WebSocketSession> technicianSessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //webSocketSessions.add(session);
        String uri = session.getUri().toString();
        Long technicianId = extractTechnicianIdFromUri(uri);

        technicianSessions.put(technicianId, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        for (WebSocketSession webSocketSession : webSocketSessions) {
//            if (webSocketSession.isOpen()) {
//                try {
//                    webSocketSession.sendMessage(message);
//                } catch (Exception e) {
//                    // Log the error and close the session if an exception occurs
//                    webSocketSession.close(CloseStatus.SERVER_ERROR);
//                }
//            }
//        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String uri = session.getUri().toString();
        Long technicianId = extractTechnicianIdFromUri(uri);

        technicianSessions.remove(technicianId);
    }

    public void sendNotificationToTechnician(Long technicianId, String message) throws Exception {
        WebSocketSession session = technicianSessions.get(technicianId);
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(message));
        }
    }

    private Long extractTechnicianIdFromUri(String uri) {
        // Assuming the technician ID is in the last part of the URI
        String[] parts = uri.split("/");
        return Long.parseLong(parts[parts.length - 1]);
    }
}
