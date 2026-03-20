// FILE PATH: src/main/java/com/scholife1/repository/ActivityLogRepository.java

package com.scholife1.repository;

import com.scholife1.model.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    List<ActivityLog> findTop5ByOrderByCreatedAtDesc();
    List<ActivityLog> findAllByOrderByCreatedAtDesc();
}