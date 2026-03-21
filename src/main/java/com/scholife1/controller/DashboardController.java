// FILE PATH: src/main/java/com/scholife1/controller/DashboardController.java

package com.scholife1.controller;

import com.scholife1.model.Admin;
import com.scholife1.model.Organization;
import com.scholife1.model.RoleAssignment;
import com.scholife1.repository.*;
import com.scholife1.service.AdminService;
import com.scholife1.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleAssignmentRepository roleAssignmentRepository;
    @Autowired
    private ActivityLogRepository activityLogRepository;
    @Autowired
    private AdminService adminService;
    @Autowired
    private OrganizationService organizationService;

    // ── Dashboard ─────────────────────────────────────────────────────────────
    @GetMapping("/dashboard/index")
    public String dashboard(Model model, Authentication auth) {
        Admin loggedInAdmin = adminService.findByEmail(auth.getName());
        model.addAttribute("loggedInAdmin", loggedInAdmin);
        model.addAttribute("totalStudents", studentRepository.count());
        model.addAttribute("totalOrganizations", organizationRepository.count());
        model.addAttribute("totalRoles", roleRepository.count());
        model.addAttribute("totalActivities", activityLogRepository.count());

        Map<String, Long> enrollment = new LinkedHashMap<>();
        enrollment.put("BS Info Technology", studentRepository.countByProgram("BS Information Technology"));
        enrollment.put("BS Computer Science", studentRepository.countByProgram("BS Computer Science"));
        enrollment.put("BS Business Admin", studentRepository.countByProgram("BS Business Administration"));
        enrollment.put("BS Nursing", studentRepository.countByProgram("BS Nursing"));
        enrollment.put("BS Education", studentRepository.countByProgram("BS Education"));
        model.addAttribute("enrollmentByCollege", enrollment);
        model.addAttribute("recentActivities", activityLogRepository.findTop5ByOrderByCreatedAtDesc());
        model.addAttribute("currentDate",
                LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
        return "dashboard/index";
    }

    // ── Activity ──────────────────────────────────────────────────────────────
    @GetMapping("/dashboard/activity")
    public String activity(Model model, Authentication auth) {
        addAdmin(model, auth);
        model.addAttribute("logs", activityLogRepository.findAllByOrderByCreatedAtDesc());
        return "dashboard/activity";
    }

    // ── Students ──────────────────────────────────────────────────────────────
    @GetMapping("/dashboard/students")
    public String students(Model model, Authentication auth) {
        addAdmin(model, auth);
        model.addAttribute("students", studentRepository.findAll());
        return "dashboard/students";
    }

    // ── Organizations (GET) ───────────────────────────────────────────────────
    @GetMapping("/dashboard/organizations")
    public String organizations(Model model, Authentication auth) {
        addAdmin(model, auth);
        List<Organization> orgs = organizationRepository.findAll();
        model.addAttribute("organizations", orgs);
        model.addAttribute("totalOrganizations", orgs.size());
        return "dashboard/organizations";
    }

    // ── Organizations (POST) ──────────────────────────────────────────────────
    @PostMapping("/dashboard/organizations")
    @ResponseBody
    public ResponseEntity<?> createOrganization(@RequestBody Organization org) {
        try {
            if (org.getName() == null || org.getName().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Organization name is required."));
            }
            if (org.getStatus() == null) {
                org.setStatus(Organization.Status.ACTIVE);
            }
            Organization saved = organizationService.saveOrganization(org);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed to save: " + e.getMessage()));
        }
    }

    // ── Organizations (Search) ────────────────────────────────────────────────
    @GetMapping("/dashboard/organizations/search")
    @ResponseBody
    public ResponseEntity<List<Organization>> searchOrganizations(
            @RequestParam(defaultValue = "") String q,
            @RequestParam(defaultValue = "") String type) {
        return ResponseEntity.ok(organizationService.searchOrganizations(q, type));
    }

    // ── Roles (GET) ───────────────────────────────────────────────────────────
    @GetMapping("/dashboard/roles")
    public String roles(Model model, Authentication auth) {
        addAdmin(model, auth);
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("organizations", organizationRepository.findAll());
        return "dashboard/roles";
    }

    // ── Roles (POST assign) ───────────────────────────────────────────────────
    @PostMapping("/dashboard/roles/assign")
    @ResponseBody
    public ResponseEntity<?> assignRole(@RequestBody Map<String, String> payload,
                                        Authentication auth) {
        try {
            Long studentId = Long.parseLong(payload.get("studentId"));
            Long organizationId = Long.parseLong(payload.get("organizationId"));
            Long roleId = Long.parseLong(payload.get("roleId"));
            String dateStr = payload.get("effectiveDate");

            RoleAssignment ra = new RoleAssignment();
            ra.setStudent(studentRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found")));
            ra.setOrganization(organizationRepository.findById(organizationId)
                    .orElseThrow(() -> new RuntimeException("Organization not found")));
            ra.setRole(roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Role not found")));
            ra.setAssignedBy(adminService.findByEmail(auth.getName()));
            if (dateStr != null && !dateStr.isBlank()) {
                ra.setEffectiveDate(LocalDate.parse(dateStr));
            }

            roleAssignmentRepository.save(ra);
            return ResponseEntity.ok(Map.of("message", "Role assigned successfully."));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ── Messages ──────────────────────────────────────────────────────────────
    @GetMapping("/dashboard/messages")
    public String messages(Model model, Authentication auth) {
        addAdmin(model, auth);
        return "dashboard/messages";
    }

    // ── Admins ────────────────────────────────────────────────────────────────
    @GetMapping("/dashboard/admins")
    public String admins(Model model, Authentication auth) {
        addAdmin(model, auth);
        model.addAttribute("admins", adminService.getAllAdmins());
        return "dashboard/admins";
    }

    // ── Shared helper ─────────────────────────────────────────────────────────
    private void addAdmin(Model model, Authentication auth) {
        if (auth != null)
            model.addAttribute("loggedInAdmin", adminService.findByEmail(auth.getName()));
    }
}