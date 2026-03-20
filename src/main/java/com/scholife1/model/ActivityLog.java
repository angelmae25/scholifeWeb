package com.scholife1.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
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
}