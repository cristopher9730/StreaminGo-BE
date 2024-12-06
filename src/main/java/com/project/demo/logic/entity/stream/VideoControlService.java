package com.project.demo.logic.entity.stream;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
public class VideoControlService {
    private String currentStatus; // PLAY, PAUSE, etc.
    private WebSocketSession adminSession;

    public void setAdminSession(WebSocketSession session) {
        this.adminSession = session;
    }

    public WebSocketSession getAdminSession() {
        return this.adminSession;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String status) {
        this.currentStatus = status;
    }
}
