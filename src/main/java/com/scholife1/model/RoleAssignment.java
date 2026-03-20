// FILE PATH: src/main/java/com/scholife1/model/RoleAssignment.java

package com.scholife1.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "role_assignments")
public class RoleAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "assigned_by", nullable = false)
    private Admin assignedBy;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;

    @Column(name = "term_end")
    private LocalDate termEnd;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters
    public Long getId()                   { return id; }
    public Student getStudent()           { return student; }
    public Organization getOrganization() { return organization; }
    public Role getRole()                 { return role; }
    public Admin getAssignedBy()          { return assignedBy; }
    public LocalDate getEffectiveDate()   { return effectiveDate; }
    public LocalDate getTermEnd()         { return termEnd; }
    public LocalDateTime getCreatedAt()   { return createdAt; }

    // Setters
    public void setId(Long id)                          { this.id = id; }
    public void setStudent(Student student)             { this.student = student; }
    public void setOrganization(Organization org)       { this.organization = org; }
    public void setRole(Role role)                      { this.role = role; }
    public void setAssignedBy(Admin assignedBy)         { this.assignedBy = assignedBy; }
    public void setEffectiveDate(LocalDate effectiveDate){ this.effectiveDate = effectiveDate; }
    public void setTermEnd(LocalDate termEnd)           { this.termEnd = termEnd; }
    public void setCreatedAt(LocalDateTime createdAt)   { this.createdAt = createdAt; }
}