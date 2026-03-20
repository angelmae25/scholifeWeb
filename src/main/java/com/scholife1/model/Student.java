// FILE PATH: src/main/java/com/scholife1/model/Student.java

package com.scholife1.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", unique = true, nullable = false)
    private String studentId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    // Maps to student_number column (NOT NULL in your DB)
    @Column(name = "student_number")
    private String studentNumber;

    // NULL  = added by admin via web portal
    // value = registered via Flutter mobile app
    @Column(nullable = true)
    private String password;

    private String program;

    @Column(name = "year_level")
    private String yearLevel;

    private String contact;
    private String address;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Status { ACTIVE, INACTIVE, PENDING }

    public Long getId()             { return id; }
    public String getStudentId()    { return studentId; }
    public String getStudentNumber(){ return studentNumber; }
    public String getFirstName()    { return firstName; }
    public String getLastName()     { return lastName; }
    public String getEmail()        { return email; }
    public String getPassword()     { return password; }
    public String getProgram()      { return program; }
    public String getYearLevel()    { return yearLevel; }
    public String getContact()      { return contact; }
    public String getAddress()      { return address; }
    public Status getStatus()       { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setId(Long id)                        { this.id = id; }
    public void setStudentId(String studentId)        { this.studentId = studentId; }
    public void setStudentNumber(String n)            { this.studentNumber = n; }
    public void setFirstName(String firstName)        { this.firstName = firstName; }
    public void setLastName(String lastName)          { this.lastName = lastName; }
    public void setEmail(String email)                { this.email = email; }
    public void setPassword(String password)          { this.password = password; }
    public void setProgram(String program)            { this.program = program; }
    public void setYearLevel(String yearLevel)        { this.yearLevel = yearLevel; }
    public void setContact(String contact)            { this.contact = contact; }
    public void setAddress(String address)            { this.address = address; }
    public void setStatus(Status status)              { this.status = status; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}