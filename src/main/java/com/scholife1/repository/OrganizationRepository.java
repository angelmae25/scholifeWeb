// FILE PATH: src/main/java/com/scholife1/repository/OrganizationRepository.java

package com.scholife1.repository;

import com.scholife1.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    Optional<Organization> findByAcronym(String acronym);
    boolean existsByAcronym(String acronym);
}