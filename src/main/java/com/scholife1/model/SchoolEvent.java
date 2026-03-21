// FILE PATH: src/main/java/com/scholife1/model/SchoolEvent.java

package com.scholife1.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class SchoolEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "short_name", length = 20)
    private String shortName;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false)
    private LocalDate date;

    @Column(length = 150)
    private String venue;

    @Column(length = 50)
    private String category;

    @Column(length = 10)
    private String color = "#8B1A1A";

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters
    public Long getId()                 { return id; }
    public String getShortName()        { return shortName; }
    public String getFullName()         { return fullName; }
    public LocalDate getDate()          { return date; }
    public String getVenue()            { return venue; }
    public String getCategory()         { return category; }
    public String getColor()            { return color; }
    public String getDescription()      { return description; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setId(Long id)                        { this.id = id; }
    public void setShortName(String shortName)        { this.shortName = shortName; }
    public void setFullName(String fullName)          { this.fullName = fullName; }
    public void setDate(LocalDate date)               { this.date = date; }
    public void setVenue(String venue)                { this.venue = venue; }
    public void setCategory(String category)          { this.category = category; }
    public void setColor(String color)                { this.color = color; }
    public void setDescription(String description)    { this.description = description; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}