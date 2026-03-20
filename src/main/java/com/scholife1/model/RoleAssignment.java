package com.scholife1.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
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
}