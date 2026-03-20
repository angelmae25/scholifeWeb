// FILE PATH: src/main/java/com/scholife1/model/Admin.java

package com.scholife1.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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
    private String password;

    @Enumerated(EnumType.STRING)
    private AdminRole role = AdminRole.ADMIN;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "employee_number")
    private String employeeNumber;

    public enum AdminRole { SUPER_ADMIN, ADMIN, MODERATOR, VIEWER }
    public enum Status    { ACTIVE, INACTIVE }

    // Getters
    public Long getId()                 { return id; }
    public String getFirstName()        { return firstName; }
    public String getLastName()         { return lastName; }
    public String getEmail()            { return email; }
    public String getPassword()         { return password; }
    public AdminRole getRole()          { return role; }
    public Status getStatus()           { return status; }
    public LocalDateTime getLastLogin() { return lastLogin; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getEmployeeNumber()   { return employeeNumber; }

    // Setters
    public void setId(Long id)                          { this.id = id; }
    public void setFirstName(String firstName)          { this.firstName = firstName; }
    public void setLastName(String lastName)            { this.lastName = lastName; }
    public void setEmail(String email)                  { this.email = email; }
    public void setPassword(String password)            { this.password = password; }
    public void setRole(AdminRole role)                 { this.role = role; }
    public void setStatus(Status status)                { this.status = status; }
    public void setLastLogin(LocalDateTime lastLogin)   { this.lastLogin = lastLogin; }
    public void setCreatedAt(LocalDateTime createdAt)   { this.createdAt = createdAt; }
    public void setEmployeeNumber(String employeeNumber){ this.employeeNumber = employeeNumber; }
}