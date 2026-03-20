package com.scholife1.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
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
}