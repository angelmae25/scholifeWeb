// FILE PATH: src/main/java/com/scholife1/service/OrganizationService.java

package com.scholife1.service;

import com.scholife1.model.Organization;
import com.scholife1.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    // ✅ ADD THIS
    public Organization saveOrganization(Organization org) {
        return organizationRepository.save(org);
    }

    // ✅ ADD THIS (used by search endpoint in your HTML)
    public List<Organization> searchOrganizations(String q, String type) {
        return organizationRepository.findAll().stream()
                .filter(o -> q == null || q.isBlank() ||
                        o.getName().toLowerCase().contains(q.toLowerCase()) ||
                        (o.getAcronym() != null && o.getAcronym().toLowerCase().contains(q.toLowerCase())))
                .filter(o -> type == null || type.isBlank() || type.equals(o.getType()))
                .toList();
    }
}