// FILE PATH: src/main/java/com/scholife1/controller/DashboardController.java

package com.scholife1.controller;

import com.scholife1.model.Admin;
import com.scholife1.repository.*;
import com.scholife1.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class DashboardController {

    @Autowired private StudentRepository      studentRepository;
    @Autowired private OrganizationRepository organizationRepository;
    @Autowired private RoleRepository         roleRepository;
    @Autowired private ActivityLogRepository  activityLogRepository;
    @Autowired private AdminService           adminService;

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
        model.addAttribute("recentActivities",  activityLogRepository.findTop5ByOrderByCreatedAtDesc());
        model.addAttribute("currentDate",
                LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
        return "dashboard/index";
    }

    @GetMapping("/dashboard/activity")
    public String activity(Model model, Authentication auth) {
        addAdmin(model, auth);
        model.addAttribute("logs", activityLogRepository.findAllByOrderByCreatedAtDesc());
        return "dashboard/activity";
    }

    @GetMapping("/dashboard/students")
    public String students(Model model, Authentication auth) {
        addAdmin(model, auth);
        model.addAttribute("students", studentRepository.findAll());
        return "dashboard/students";
    }

    @GetMapping("/dashboard/organizations")
    public String organizations(Model model, Authentication auth) {
        addAdmin(model, auth);
        model.addAttribute("organizations", organizationRepository.findAll());
        return "dashboard/organizations";
    }

    @GetMapping("/dashboard/roles")
    public String roles(Model model, Authentication auth) {
        addAdmin(model, auth);
        model.addAttribute("roles", roleRepository.findAll());
        return "dashboard/roles";
    }

    @GetMapping("/dashboard/messages")
    public String messages(Model model, Authentication auth) {
        addAdmin(model, auth);
        return "dashboard/messages";
    }

    @GetMapping("/dashboard/admins")
    public String admins(Model model, Authentication auth) {
        addAdmin(model, auth);
        model.addAttribute("admins", adminService.getAllAdmins());
        return "dashboard/admins";
    }

    private void addAdmin(Model model, Authentication auth) {
        if (auth != null)
            model.addAttribute("loggedInAdmin", adminService.findByEmail(auth.getName()));
    }
}