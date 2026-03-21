// FILE PATH: src/main/java/com/scholife1/repository/EventRepository.java

package com.scholife1.repository;

import com.scholife1.model.SchoolEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<SchoolEvent, Long> {
    List<SchoolEvent> findAllByOrderByDateAsc();
}