// FILE PATH: src/main/java/com/scholife1/repository/MessageRepository.java

package com.scholife1.repository;

import com.scholife1.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findBySenderIdOrReceiverIdOrderBySentAtDesc(Long senderId, Long receiverId);
    long countByReceiverIdAndIsReadFalse(Long receiverId);
}