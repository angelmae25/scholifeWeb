package com.scholife1.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String body;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NewsCategory category = NewsCategory.CAMPUS;

    @Column(name = "published_at")
    private LocalDateTime publishedAt = LocalDateTime.now();

    @Column(name = "is_featured")
    private Boolean isFeatured = false;

    @Column(name = "image_url", length = 300)
    private String imageUrl;

    @Column(name = "author_name", length = 100)
    private String authorName = "Scholife Editorial";

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum NewsCategory { ALL, HEALTH, ACADEMIC, CAMPUS, SPORTS }

    public Long getId()                   { return id; }
    public String getTitle()              { return title; }
    public String getBody()               { return body; }
    public NewsCategory getCategory()     { return category; }
    public LocalDateTime getPublishedAt() { return publishedAt; }
    public Boolean getIsFeatured()        { return isFeatured; }
    public String getImageUrl()           { return imageUrl; }
    public String getAuthorName()         { return authorName; }
    public LocalDateTime getCreatedAt()   { return createdAt; }

    public void setId(Long id)                        { this.id = id; }
    public void setTitle(String title)                { this.title = title; }
    public void setBody(String body)                  { this.body = body; }
    public void setCategory(String cat)               {
        try { this.category = NewsCategory.valueOf(cat.toUpperCase()); }
        catch (Exception e) { this.category = NewsCategory.CAMPUS; }
    }
    public void setPublishedAt(LocalDateTime t)       { this.publishedAt = t; }
    public void setIsFeatured(Boolean isFeatured)     { this.isFeatured = isFeatured; }
    public void setImageUrl(String imageUrl)          { this.imageUrl = imageUrl; }
    public void setAuthorName(String authorName)      { this.authorName = authorName; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}