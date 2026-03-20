package com.scholife1.repository;

import com.scholife1.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    List<Organization> findByType(String type);

    List<Organization> findByStatus(Organization.Status status);

}