// FILE PATH: src/main/java/com/scholife1/model/Message.java

package com.scholife1.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private Admin sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private Admin receiver;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String body;

    @Column(name = "is_read")
    private Boolean isRead = false;

    @Column(name = "sent_at")
    private LocalDateTime sentAt = LocalDateTime.now();

    // Getters
    public Long getId()              { return id; }
    public Admin getSender()         { return sender; }
    public Admin getReceiver()       { return receiver; }
    public String getBody()          { return body; }
    public Boolean getIsRead()       { return isRead; }
    public LocalDateTime getSentAt() { return sentAt; }

    // Setters
    public void setId(Long id)              { this.id = id; }
    public void setSender(Admin sender)     { this.sender = sender; }
    public void setReceiver(Admin receiver) { this.receiver = receiver; }
    public void setBody(String body)        { this.body = body; }
    public void setIsRead(Boolean isRead)   { this.isRead = isRead; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
}