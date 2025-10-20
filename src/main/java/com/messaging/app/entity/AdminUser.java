package com.messaging.app.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin_users")
public class AdminUser {
    @Id
    private String id = "admin"; // Single admin user
    
    @Enumerated(EnumType.STRING)
    private AdminStatus status = AdminStatus.OFFLINE;
    
    private LocalDateTime lastOnline;
    private String currentSessionId;
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public AdminStatus getStatus() { return status; }
    public void setStatus(AdminStatus status) { this.status = status; }
    
    public LocalDateTime getLastOnline() { return lastOnline; }
    public void setLastOnline(LocalDateTime lastOnline) { this.lastOnline = lastOnline; }
    
    public String getCurrentSessionId() { return currentSessionId; }
    public void setCurrentSessionId(String currentSessionId) { this.currentSessionId = currentSessionId; }
}

