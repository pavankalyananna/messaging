package com.messaging.app.dto;

import java.time.LocalDateTime;

public class WebSocketMessage {
    private String type;
    private String from;
    private String to;
    private Object data;
    private String timestamp;

    public WebSocketMessage() {
        this.timestamp = LocalDateTime.now().toString();
    }

    public WebSocketMessage(String type, String from, String to, Object data) {
        this();
        this.type = type;
        this.from = from;
        this.to = to;
        this.data = data;
    }

    // Getters and Setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }
    
    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }
    
    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
    
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}