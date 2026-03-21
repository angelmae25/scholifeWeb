// FILE PATH: src/main/java/com/scholife1/controller/OrgPostController.java

package com.scholife1.controller;

import com.scholife1.model.*;
import com.scholife1.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/org-post")
@CrossOrigin(origins = "*")
public class OrgpostController {

    @Autowired private RoleAssignmentRepository roleAssignmentRepo;
    @Autowired private StudentRepository         studentRepo;
    @Autowired private NewsRepository            newsRepo;
    @Autowired private EventRepository           eventRepo;

    // Test in browser: http://YOUR_PC_IP:8080/api/org-post/ping
    @GetMapping("/ping")
    public ResponseEntity<?> ping() {
        Map<String, Object> res = new HashMap<>();
        res.put("status",  "ok");
        res.put("message", "OrgPost API is running");
        return ResponseEntity.ok(res);
    }

    // Test in browser: http://YOUR_PC_IP:8080/api/org-post/my-organizations?studentId=XXXX
    @GetMapping("/my-organizations")
    public ResponseEntity<?> getMyOrganizations(@RequestParam String studentId) {
        System.out.println("[OrgPost] called with studentId='" + studentId + "'");

        Student student = studentRepo.findByStudentId(studentId).orElse(null);
        if (student == null) {
            System.out.println("[OrgPost] NO student found. Check: SELECT * FROM students WHERE student_id='" + studentId + "'");
            return ResponseEntity.ok(new ArrayList<>());
        }

        System.out.println("[OrgPost] Student found: " + student.getFirstName() + " id=" + student.getId());

        List<RoleAssignment> assignments = roleAssignmentRepo.findByStudentId(student.getId());
        System.out.println("[OrgPost] Role assignments found: " + assignments.size());
        if (assignments.isEmpty()) {
            System.out.println("[OrgPost] Check: SELECT * FROM role_assignments WHERE student_id=" + student.getId());
        }

        List<Map<String, Object>> result = assignments.stream().map(a -> {
            Map<String, Object> map = new HashMap<>();
            map.put("assignmentId",     a.getId());
            map.put("organizationId",   a.getOrganization().getId());
            map.put("organizationName", a.getOrganization().getName());
            map.put("acronym",          a.getOrganization().getAcronym() != null ? a.getOrganization().getAcronym() : "");
            map.put("roleName",         a.getRole().getRoleName());
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/news")
    public ResponseEntity<?> postNews(@RequestBody Map<String, Object> body) {
        String  studentId  = (String)  body.get("studentId");
        int     orgId      = ((Number) body.get("organizationId")).intValue();
        String  title      = (String)  body.get("title");
        String  newsBody   = (String)  body.get("body");
        String  category   = (String)  body.getOrDefault("category", "CAMPUS");
        boolean isFeatured = Boolean.TRUE.equals(body.get("isFeatured"));

        Student student = studentRepo.findByStudentId(studentId).orElse(null);
        if (student == null) {
            Map<String, Object> err = new HashMap<>();
            err.put("message", "Student not found.");
            return ResponseEntity.status(403).body(err);
        }

        boolean hasRole = roleAssignmentRepo.findByStudentId(student.getId())
                .stream().anyMatch(a -> a.getOrganization().getId().intValue() == orgId);
        if (!hasRole) {
            Map<String, Object> err = new HashMap<>();
            err.put("message", "Not assigned to this organization.");
            return ResponseEntity.status(403).body(err);
        }

        News news = new News();
        news.setTitle(title);
        news.setBody(newsBody);
        news.setCategory(category);
        news.setIsFeatured(isFeatured);
        news.setAuthorName(student.getFirstName() + " " + student.getLastName());
        newsRepo.save(news);

        Map<String, Object> ok = new HashMap<>();
        ok.put("message", "News posted successfully.");
        return ResponseEntity.status(201).body(ok);
    }

    @PostMapping("/events")
    public ResponseEntity<?> postEvent(@RequestBody Map<String, Object> body) {
        String studentId   = (String)  body.get("studentId");
        int    orgId       = ((Number) body.get("organizationId")).intValue();
        String shortName   = (String)  body.getOrDefault("shortName", "");
        String fullName    = (String)  body.get("fullName");
        String dateStr     = (String)  body.get("date");
        String venue       = (String)  body.getOrDefault("venue", "");
        String category    = (String)  body.getOrDefault("category", "General");
        String description = (String)  body.getOrDefault("description", "");
        String color       = (String)  body.getOrDefault("color", "#8B1A1A");

        Student student = studentRepo.findByStudentId(studentId).orElse(null);
        if (student == null) {
            Map<String, Object> err = new HashMap<>();
            err.put("message", "Student not found.");
            return ResponseEntity.status(403).body(err);
        }

        boolean hasRole = roleAssignmentRepo.findByStudentId(student.getId())
                .stream().anyMatch(a -> a.getOrganization().getId().intValue() == orgId);
        if (!hasRole) {
            Map<String, Object> err = new HashMap<>();
            err.put("message", "Not assigned to this organization.");
            return ResponseEntity.status(403).body(err);
        }

        SchoolEvent event = new SchoolEvent();
        event.setShortName(shortName);
        event.setFullName(fullName);
        event.setDate(LocalDate.parse(dateStr));
        event.setVenue(venue);
        event.setCategory(category);
        event.setDescription(description);
        event.setColor(color);
        eventRepo.save(event);

        Map<String, Object> ok = new HashMap<>();
        ok.put("message", "Event posted successfully.");
        return ResponseEntity.status(201).body(ok);
    }
}