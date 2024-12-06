package com.project.demo.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class VideoControlHandler extends TextWebSocketHandler {
    private Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();
    private String currentStatus = "PAUSE"; // Default status
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        // Enviar el estado actual del video a los nuevos clientes en formato JSON
        sendStatusToClient(session, currentStatus);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        try {
            String payload = message.getPayload();
            Map<String, Object> msg = objectMapper.readValue(payload, Map.class);

            String type = (String) msg.get("type");
            if ("videoControl".equals(type)) {
                currentStatus = (String) msg.get("status");
                System.out.println("Received video control command: " + currentStatus);
                broadcastMessage(new VideoControlMessage("videoControl", currentStatus));
            } else if ("chat".equals(type)) {
                broadcastMessage(msg);
            } else if ("reaction".equals(type)) {
                broadcastMessage(msg);
            }
        } catch (Exception e) {
            System.err.println("Error handling text message: " + e.getMessage());
        }
    }

    private void sendStatusToClient(WebSocketSession session, String status) throws IOException {
        String jsonMessage = objectMapper.writeValueAsString(new VideoControlMessage("videoControl", status));
        session.sendMessage(new TextMessage(jsonMessage));
    }

    private void broadcastMessage(Object message) throws IOException {
        String jsonMessage = objectMapper.writeValueAsString(message);
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(jsonMessage));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
        sessions.remove(session);
        System.out.println("Session closed: " + session.getId());
    }

    // Clase auxiliar para crear mensajes JSON
    private static class VideoControlMessage {
        private String type;
        private String status;

        public VideoControlMessage(String type, String status) {
            this.type = type;
            this.status = status;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
