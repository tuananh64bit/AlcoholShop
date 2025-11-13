package com.example.alcoholshop.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * ContactMessage model class representing contact form submissions
 */
public class ContactMessage {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String subject;
    private String message;
    private String status; // NEW / READ / REPLIED etc.
    private LocalDateTime createdAt;
    
    // Constructors
    public ContactMessage() {}
    
    public ContactMessage(String name, String email, String phone, String subject, String message) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.subject = subject;
        this.message = message;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    /**
     * Return createdAt as java.util.Date for JSTL fmt:formatDate compatibility
     */
    public Date getCreatedAtAsDate() {
        if (this.createdAt == null) return null;
        return Date.from(this.createdAt.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Get formatted creation date
     */
    public String getFormattedCreatedAt() {
        if (createdAt == null) {
            return "";
        }
        return createdAt.toString().replace("T", " ").substring(0, 19);
    }
    
    /**
     * Get truncated message for display
     */
    public String getTruncatedMessage(int maxLength) {
        if (message == null) {
            return "";
        }
        if (message.length() <= maxLength) {
            return message;
        }
        return message.substring(0, maxLength) + "...";
    }
    
    @Override
    public String toString() {
        return "ContactMessage{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", subject='" + subject + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
