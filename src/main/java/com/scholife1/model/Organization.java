// FILE PATH: src/main/java/com/scholife1/model/Organization.java

package com.scholife1.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "organizations")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String acronym;
    private String type;
    private String adviser;

    @Column(name = "year_founded")
    private Integer yearFounded;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Status { ACTIVE, INACTIVE }

    // Getters
    public Long getId()                 { return id; }
    public String getName()             { return name; }
    public String getAcronym()          { return acronym; }
    public String getType()             { return type; }
    public String getAdviser()          { return adviser; }
    public Integer getYearFounded()     { return yearFounded; }
    public String getDescription()      { return description; }
    public Status getStatus()           { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setId(Long id)                        { this.id = id; }
    public void setName(String name)                  { this.name = name; }
    public void setAcronym(String acronym)            { this.acronym = acronym; }
    public void setType(String type)                  { this.type = type; }
    public void setAdviser(String adviser)            { this.adviser = adviser; }
    public void setYearFounded(Integer yearFounded)   { this.yearFounded = yearFounded; }
    public void setDescription(String description)    { this.description = description; }
    public void setStatus(Status status)              { this.status = status; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}