package com.scholife1.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "admins")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;   // BCrypt hashed

    @Enumerated(EnumType.STRING)
    private AdminRole role = AdminRole.ADMIN;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum AdminRole { SUPER_ADMIN, ADMIN, MODERATOR, VIEWER }
    public enum Status    { ACTIVE, INACTIVE }

    @Column(name = "employee_number", nullable = true)
    private String employeeNumber;

}