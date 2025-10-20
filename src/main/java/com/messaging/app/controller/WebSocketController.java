package com.messaging.app.controller;

import com.messaging.app.service.AdminService;
import com.messaging.app.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;

@Controller
public class WebSocketController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private WebSocketService webSocketService;

    @MessageMapping("/admin.presence")
    public void handleAdminPresence(@Payload Map<String, Object> message, Principal principal) {
        String sessionId = principal != null ? principal.getName() : "unknown";
        String type = (String) message.get("type");
        
        System.out.println("Admin presence update: " + type + " from session: " + sessionId);
        
        switch (type) {
            case "ADMIN_ONLINE":
                adminService.setAdminOnline(sessionId);
                break;
            case "ADMIN_OFFLINE":
                adminService.setAdminOffline();
                break;
            case "ADMIN_BUSY":
                adminService.setAdminBusy();
                break;
        }
        
        webSocketService.notifyAdminStatusChanged();
    }

    @MessageMapping("/call.signaling")
    public void handleCallSignaling(@Payload Map<String, Object> signalingMessage, Principal principal) {
        String from = principal != null ? principal.getName() : "unknown";
        String type = (String) signalingMessage.get("type");
        String to = (String) signalingMessage.get("to");
        
        System.out.println("Call signaling - From: " + from + ", To: " + to + ", Type: " + type);
        
        // Add from field to the signaling message
        signalingMessage.put("from", from);
        
        if ("admin".equals(to)) {
            webSocketService.sendCallSignalingToAdmin(signalingMessage);
        } else {
            webSocketService.sendCallSignalingToVisitor(signalingMessage);
        }
    }

    @MessageMapping("/call.signal")
    public void handleCallSignal(@Payload Map<String, Object> signal, Principal principal) {
        String from = principal != null ? principal.getName() : "unknown";
        String to = (String) signal.get("to");
        String type = (String) signal.get("type");
        
        System.out.println("Call signal - From: " + from + ", To: " + to + ", Type: " + type);
        
        // Simply forward the signal to the target
        if ("admin".equals(to)) {
            webSocketService.sendToAdmin(signal);
        } else {
            webSocketService.sendToVisitor(to, signal);
        }
    }
}