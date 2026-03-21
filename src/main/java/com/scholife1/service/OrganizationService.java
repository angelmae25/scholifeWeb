// FILE PATH: src/main/java/com/scholife1/service/OrganizationService.java

package com.scholife1.service;

import com.scholife1.model.Organization;
import com.scholife1.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    public Organization saveOrganization(Organization org) {
        return organizationRepository.save(org);
    }

    public void deleteOrganization(Long id) {
        organizationRepository.deleteById(id);
    }

    public List<Organization> searchOrganizations(String query, String type) {
        List<Organization> all = organizationRepository.findAll();

        return all.stream()
                .filter(o -> {
                    boolean matchQ = query == null || query.isBlank()
                            || o.getName().toLowerCase().contains(query.toLowerCase())
                            || (o.getAcronym() != null && o.getAcronym().toLowerCase().contains(query.toLowerCase()));
                    boolean matchT = type == null || type.isBlank()
                            || (o.getType() != null && o.getType().equalsIgnoreCase(type));
                    return matchQ && matchT;
                })
                .collect(Collectors.toList());
    }
}