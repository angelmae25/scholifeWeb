// FILE PATH: src/main/java/com/scholife1/repository/AdminRepository.java

package com.scholife1.repository;

import com.scholife1.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByEmail(String email);
}