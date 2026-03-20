package com.scholife1.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class MessageDTO {
    private Long id;
    private Long receiverId;
    private String receiverName;
    private String senderName;
    private String subject;
    private String body;
    private boolean isRead;
    private String sentAt;
}