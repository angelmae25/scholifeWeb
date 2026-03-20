// FILE PATH: src/main/java/com/scholife1/model/ActivityLog.java
//hi
package com.scholife1.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_log")
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    private String event;
    private String entity;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "ip_address")
    private String ipAddress;

    @Enumerated(EnumType.STRING)
    private LogStatus status = LogStatus.SUCCESS;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Category  { STUDENTS, ORGANIZATIONS, ROLES, SYSTEM, MESSAGES }
    public enum LogStatus { SUCCESS, FAILED, PENDING }

    // Getters
    public Long getId()                 { return id; }
    public Admin getAdmin()             { return admin; }
    public String getEvent()            { return event; }
    public String getEntity()           { return entity; }
    public Category getCategory()       { return category; }
    public String getIpAddress()        { return ipAddress; }
    public LogStatus getStatus()        { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setId(Long id)                        { this.id = id; }
    public void setAdmin(Admin admin)                 { this.admin = admin; }
    public void setEvent(String event)                { this.event = event; }
    public void setEntity(String entity)              { this.entity = entity; }
    public void setCategory(Category category)        { this.category = category; }
    public void setIpAddress(String ipAddress)        { this.ipAddress = ipAddress; }
    public void setStatus(LogStatus status)           { this.status = status; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}