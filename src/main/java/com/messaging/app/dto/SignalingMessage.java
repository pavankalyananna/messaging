package com.messaging.app.dto;

public class SignalingMessage {
    private String requestId;
    private String type; // offer, answer, candidate, hangup
    private String from;
    private String to;
    private Object sdp;
    private Object candidate;
    
    // Getters and Setters
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }
    
    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }
    
    public Object getSdp() { return sdp; }
    public void setSdp(Object sdp) { this.sdp = sdp; }
    
    public Object getCandidate() { return candidate; }
    public void setCandidate(Object candidate) { this.candidate = candidate; }
}