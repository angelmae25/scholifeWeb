// FILE PATH: src/main/java/com/scholife1/service/RoleService.java

package com.scholife1.service;

import com.scholife1.model.*;
import com.scholife1.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class RoleService {

    @Autowired private RoleRepository           roleRepository;
    @Autowired private RoleAssignmentRepository roleAssignmentRepository;
    @Autowired private StudentRepository        studentRepository;
    @Autowired private OrganizationRepository   organizationRepository;
    @Autowired private AdminRepository          adminRepository;
    @Autowired private ActivityLogRepository    activityLogRepository;

    // ── Fetch all roles ──────────────────────────────────────────────────────
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    // ── Count assignments per role ───────────────────────────────────────────
    public Map<String, Long> getRoleDistribution() {
        List<Role> roles = roleRepository.findAll();
        Map<String, Long> dist = new java.util.LinkedHashMap<>();
        for (Role r : roles) {
            long count = roleAssignmentRepository.findAll().stream()
                    .filter(ra -> ra.getRole().getId().equals(r.getId()))
                    .count();
            dist.put(r.getRoleName(), count);
        }
        return dist;
    }

    // ── Assign role ──────────────────────────────────────────────────────────
    public RoleAssignment assign(Long studentId,
                                 Long organizationId,
                                 Long roleId,
                                 String effectiveDateStr,
                                 String adminEmail,
                                 HttpServletRequest request) {

        Student      student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Organization org     = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        Role         role    = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        Admin        admin   = adminRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        RoleAssignment ra = new RoleAssignment();
        ra.setStudent(student);
        ra.setOrganization(org);
        ra.setRole(role);
        ra.setAssignedBy(admin);
        if (effectiveDateStr != null && !effectiveDateStr.isBlank()) {
            ra.setEffectiveDate(LocalDate.parse(effectiveDateStr));
        }
        roleAssignmentRepository.save(ra);

        // ── Log to activity_log ──────────────────────────────────────────────
        ActivityLog log = new ActivityLog();
        log.setAdmin(admin);
        log.setEvent("Role assigned");
        log.setEntity(student.getFirstName() + " " + student.getLastName()
                + " → " + role.getRoleName()
                + " (" + org.getName() + ")");
        log.setCategory(ActivityLog.Category.ROLES);
        log.setIpAddress(getClientIp(request));
        log.setStatus(ActivityLog.LogStatus.SUCCESS);
        activityLogRepository.save(log);

        return ra;
    }

    // ── Helper: get real client IP ───────────────────────────────────────────
    private String getClientIp(HttpServletRequest request) {
        String xff = request.getHeader("X-Forwarded-For");
        return (xff != null && !xff.isBlank())
                ? xff.split(",")[0].trim()
                : request.getRemoteAddr();
    }
}