// FILE PATH: src/main/java/com/scholife1/repository/RoleRepository.java

package com.scholife1.repository;

import com.scholife1.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Role model uses "roleName" not "name"
    Optional<Role> findByRoleName(String roleName);
    boolean existsByRoleName(String roleName);
}