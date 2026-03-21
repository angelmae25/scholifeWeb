// FILE PATH: src/main/java/com/scholife1/controller/DashboardController.java

package com.scholife1.controller;

import com.scholife1.model.*;
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
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    @Autowired private StudentRepository      studentRepository;
    @Autowired private OrganizationRepository organizationRepository;
    @Autowired private RoleRepository         roleRepository;
    @Autowired private RoleAssignmentRepository roleAssignmentRepository;
    @Autowired private ActivityLogRepository  activityLogRepository;
    @Autowired private AdminService           adminService;
    @Autowired private OrganizationService    organizationService;

    // ── Dashboard ─────────────────────────────────────────────────────────────
    @GetMapping("/dashboard/index")
    public String dashboard(Model model, Authentication auth) {
        Admin loggedInAdmin = adminService.findByEmail(auth.getName());
        model.addAttribute("loggedInAdmin", loggedInAdmin);
        model.addAttribute("totalStudents",      studentRepository.count());
        model.addAttribute("totalOrganizations", organizationRepository.count());
        model.addAttribute("totalRoles",         roleRepository.count());
        model.addAttribute("totalActivities",    activityLogRepository.count());

        Map<String, Long> enrollment = new LinkedHashMap<>();
        enrollment.put("BS Info Technology",  studentRepository.countByProgram("BS Information Technology"));
        enrollment.put("BS Computer Science", studentRepository.countByProgram("BS Computer Science"));
        enrollment.put("BS Business Admin",   studentRepository.countByProgram("BS Business Administration"));
        enrollment.put("BS Nursing",          studentRepository.countByProgram("BS Nursing"));
        enrollment.put("BS Education",        studentRepository.countByProgram("BS Education"));
        model.addAttribute("enrollmentByCollege", enrollment);
        model.addAttribute("recentActivities",
                activityLogRepository.findTop5ByOrderByCreatedAtDesc());
        model.addAttribute("currentDate",
                LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
        return "dashboard/index";
    }

    // ── Activity ──────────────────────────────────────────────────────────────
    @GetMapping("/dashboard/activity")
    public String activity(Model model, Authentication auth) {
        addAdmin(model, auth);
        model.addAttribute("activityLogs",
                activityLogRepository.findAllByOrderByCreatedAtDesc());
        return "dashboard/activity";
    }

    // ── Students ──────────────────────────────────────────────────────────────
    @GetMapping("/dashboard/students")
    public String students(Model model, Authentication auth) {
        addAdmin(model, auth);
        List<Student> students = studentRepository.findAll();
        model.addAttribute("students", students);
        model.addAttribute("totalStudents", students.size());
        return "dashboard/students";
    }

    // ── Organizations GET ─────────────────────────────────────────────────────
    @GetMapping("/dashboard/organizations")
    public String organizations(Model model, Authentication auth) {
        addAdmin(model, auth);
        List<Organization> orgs = organizationRepository.findAll();
        model.addAttribute("organizations", orgs);
        model.addAttribute("totalOrganizations", orgs.size());
        return "dashboard/organizations";
    }

    // ── Organizations POST (create) ───────────────────────────────────────────
    @PostMapping("/dashboard/organizations")
    @ResponseBody
    public ResponseEntity<?> createOrganization(@RequestBody Organization org,
                                                Authentication auth) {
        try {
            if (org.getName() == null || org.getName().isBlank())
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Organization name is required."));
            if (org.getStatus() == null) org.setStatus(Organization.Status.ACTIVE);
            Organization saved = organizationService.saveOrganization(org);

            // Log to activity_log
            logActivity("Organization created", saved.getName(),
                    ActivityLog.Category.ORGANIZATIONS, ActivityLog.LogStatus.SUCCESS,
                    adminService.findByEmail(auth.getName()), null);

            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Failed: " + e.getMessage()));
        }
    }

    // ── Organizations Search ──────────────────────────────────────────────────
    @GetMapping("/dashboard/organizations/search")
    @ResponseBody
    public ResponseEntity<List<Organization>> searchOrganizations(
            @RequestParam(defaultValue = "") String q,
            @RequestParam(defaultValue = "") String type) {
        return ResponseEntity.ok(organizationService.searchOrganizations(q, type));
    }

    // ── Roles GET ─────────────────────────────────────────────────────────────
    @GetMapping("/dashboard/roles")
    public String roles(Model model, Authentication auth) {
        addAdmin(model, auth);

        List<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);
        model.addAttribute("students",      studentRepository.findAll());
        model.addAttribute("organizations", organizationRepository.findAll());

        // Count assignments per role for the distribution chart
        List<RoleAssignment> allAssignments = roleAssignmentRepository.findAll();
        Map<Long, Long> counts = allAssignments.stream()
                .collect(Collectors.groupingBy(
                        ra -> ra.getRole().getId(),
                        Collectors.counting()
                ));
        model.addAttribute("roleAssignmentCounts", counts);
        model.addAttribute("totalAssignments",
                counts.values().stream().mapToLong(Long::longValue).sum());

        // Recent assignments (latest 20)
        List<RoleAssignment> recent = allAssignments.stream()
                .sorted(Comparator.comparing(RoleAssignment::getCreatedAt).reversed())
                .limit(20)
                .collect(Collectors.toList());
        model.addAttribute("recentAssignments", recent);

        return "dashboard/roles";
    }

    // ── Roles POST (assign) ───────────────────────────────────────────────────
    @PostMapping("/dashboard/roles/assign")
    @ResponseBody
    public ResponseEntity<?> assignRole(@RequestBody Map<String, String> payload,
                                        Authentication auth) {
        try {
            Long studentId      = Long.parseLong(payload.get("studentId"));
            Long organizationId = Long.parseLong(payload.get("organizationId"));
            Long roleId         = Long.parseLong(payload.get("roleId"));
            String dateStr      = payload.get("effectiveDate");

            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));
            Organization org = organizationRepository.findById(organizationId)
                    .orElseThrow(() -> new RuntimeException("Organization not found"));
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            Admin admin = adminService.findByEmail(auth.getName());

            RoleAssignment ra = new RoleAssignment();
            ra.setStudent(student);
            ra.setOrganization(org);
            ra.setRole(role);
            ra.setAssignedBy(admin);
            if (dateStr != null && !dateStr.isBlank()) {
                ra.setEffectiveDate(LocalDate.parse(dateStr));
            }

            roleAssignmentRepository.save(ra);

            // ── Log to activity_log so it shows in Activity page ─────────────
            String entity = student.getFirstName() + " " + student.getLastName()
                    + " → " + role.getRoleName()
                    + " (" + org.getName() + ")";
            logActivity("Role assigned", entity,
                    ActivityLog.Category.ROLES, ActivityLog.LogStatus.SUCCESS,
                    admin, null);

            return ResponseEntity.ok(Map.of("message", "Role assigned successfully."));

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid ID format."));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ── Roles DELETE (remove assignment) ──────────────────────────────────────
    @DeleteMapping("/dashboard/roles/assignments/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteAssignment(@PathVariable Long id,
                                              Authentication auth) {
        try {
            RoleAssignment ra = roleAssignmentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Assignment not found"));

            String entity = ra.getStudent().getFirstName() + " " + ra.getStudent().getLastName()
                    + " → " + ra.getRole().getRoleName();
            Admin admin = adminService.findByEmail(auth.getName());

            roleAssignmentRepository.deleteById(id);

            // Log removal
            logActivity("Role removed", entity,
                    ActivityLog.Category.ROLES, ActivityLog.LogStatus.SUCCESS,
                    admin, null);

            return ResponseEntity.ok(Map.of("message", "Assignment removed."));
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

    // ── Shared helpers ────────────────────────────────────────────────────────
    private void addAdmin(Model model, Authentication auth) {
        if (auth != null)
            model.addAttribute("loggedInAdmin",
                    adminService.findByEmail(auth.getName()));
    }

    private void logActivity(String event, String entity,
                             ActivityLog.Category category,
                             ActivityLog.LogStatus status,
                             Admin admin, String ip) {
        try {
            ActivityLog log = new ActivityLog();
            log.setEvent(event);
            log.setEntity(entity);
            log.setCategory(category);
            log.setStatus(status);
            log.setAdmin(admin);
            log.setIpAddress(ip);
            activityLogRepository.save(log);
        } catch (Exception ignored) {}
    }
}