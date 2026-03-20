package com.scholife1.repository;

import com.scholife1.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderIdOrReceiverIdOrderBySentAtDesc(Long senderId, Long receiverId);
    long countByReceiverIdAndIsReadFalse(Long receiverId);
}