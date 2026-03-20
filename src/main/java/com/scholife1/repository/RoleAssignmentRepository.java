// FILE PATH: src/main/java/com/scholife1/repository/RoleAssignmentRepository.java

package com.scholife1.repository;

import com.scholife1.model.RoleAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RoleAssignmentRepository extends JpaRepository<RoleAssignment, Long> {

    List<RoleAssignment> findByStudentId(Long studentId);
    List<RoleAssignment> findByOrganizationId(Long orgId);
}