package com.messaging.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class WebSocketService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private AdminService adminService;

    public void notifyCallRequestToAdmin(String visitorName) {
        try {
            System.out.println("=== SENDING CALL REQUEST TO ADMIN ===");
            System.out.println("Visitor: " + visitorName);
            
            Map<String, Object> message = new HashMap<>();
            message.put("type", "NEW_CALL_REQUEST");
            message.put("from", visitorName);
            message.put("to", "admin");
            message.put("data", "Incoming call from " + visitorName);
            message.put("timestamp", LocalDateTime.now().toString());
            
            messagingTemplate.convertAndSend("/topic/admin/requests", message);
            messagingTemplate.convertAndSend("/topic/admin/notifications", message);
            
            System.out.println("Call request sent successfully");
            
        } catch (Exception e) {
            System.err.println("ERROR in notifyCallRequestToAdmin: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void notifyNewRequestToAdmin(String visitorName, String messageContent) {
        try {
            System.out.println("=== SENDING MESSAGE REQUEST TO ADMIN ===");
            System.out.println("Visitor: " + visitorName + ", Message: " + messageContent);
            
            Map<String, Object> message = new HashMap<>();
            message.put("type", "NEW_MESSAGE_REQUEST");
            message.put("from", visitorName);
            message.put("to", "admin");
            message.put("data", messageContent);
            message.put("timestamp", LocalDateTime.now().toString());
            
            messagingTemplate.convertAndSend("/topic/admin/requests", message);
            messagingTemplate.convertAndSend("/topic/admin/notifications", message);
            
            System.out.println("Message request sent successfully");
            
        } catch (Exception e) {
            System.err.println("ERROR in notifyNewRequestToAdmin: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendCallSignalingToAdmin(Map<String, Object> signalingMessage) {
        try {
            System.out.println("=== SENDING SIGNALING TO ADMIN ===");
            System.out.println("Type: " + signalingMessage.get("type"));
            
            messagingTemplate.convertAndSend("/topic/admin/call", signalingMessage);
            
        } catch (Exception e) {
            System.err.println("ERROR sending signaling to admin: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendCallSignalingToVisitor(Map<String, Object> signalingMessage) {
        try {
            String to = (String) signalingMessage.get("to");
            System.out.println("=== SENDING SIGNALING TO VISITOR ===");
            System.out.println("To: " + to + ", Type: " + signalingMessage.get("type"));
            
            messagingTemplate.convertAndSend("/topic/visitor/call/" + to, signalingMessage);
            
        } catch (Exception e) {
            System.err.println("ERROR sending signaling to visitor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void notifyAdminStatusChanged() {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", "ADMIN_STATUS_CHANGED");
            message.put("from", "system");
            message.put("data", adminService.getAdminStatus());
            message.put("timestamp", LocalDateTime.now().toString());
            
            messagingTemplate.convertAndSend("/topic/admin/status", message);
            messagingTemplate.convertAndSend("/topic/visitor/status", message);
            
        } catch (Exception e) {
            System.err.println("ERROR notifying status change: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void notifyCallAccepted(String visitorName, String requestId) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", "CALL_ACCEPTED");
            message.put("from", "admin");
            message.put("to", requestId);
            message.put("data", "Call accepted by admin");
            message.put("timestamp", LocalDateTime.now().toString());
            
            System.out.println("=== NOTIFYING CALL ACCEPTED ===");
            System.out.println("To request: " + requestId);
            
            messagingTemplate.convertAndSend("/topic/visitor/call/" + requestId, message);
            
        } catch (Exception e) {
            System.err.println("ERROR notifying call accepted: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void notifyCallRejected(String visitorName, String requestId, String reason) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", "CALL_REJECTED");
            message.put("from", "admin");
            message.put("to", requestId);
            message.put("data", reason);
            message.put("timestamp", LocalDateTime.now().toString());
            
            messagingTemplate.convertAndSend("/topic/visitor/call/" + requestId, message);
            
        } catch (Exception e) {
            System.err.println("ERROR notifying call rejected: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendToAdmin(Map<String, Object> signal) {
        try {
            System.out.println("Sending to admin: " + signal.get("type"));
            messagingTemplate.convertAndSend("/topic/admin/signals", signal);
            
        } catch (Exception e) {
            System.err.println("ERROR in sendToAdmin: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendToVisitor(String visitorId, Map<String, Object> signal) {
        try {
            System.out.println("Sending to visitor " + visitorId + ": " + signal.get("type"));
            messagingTemplate.convertAndSend("/topic/visitor/" + visitorId + "/signals", signal);
            
        } catch (Exception e) {
            System.err.println("ERROR in sendToVisitor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}