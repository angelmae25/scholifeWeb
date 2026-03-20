package com.scholife1.repository;

import com.scholife1.model.RoleAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoleAssignmentRepository extends JpaRepository<RoleAssignment, Long> {

    List<RoleAssignment> findByStudentId(Long studentId);

    List<RoleAssignment> findByOrganizationId(Long orgId);

}