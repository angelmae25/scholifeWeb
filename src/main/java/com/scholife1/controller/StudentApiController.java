// FILE PATH: src/main/java/com/scholife1/controller/StudentApiController.java

package com.scholife1.controller;

import com.scholife1.model.ActivityLog;
import com.scholife1.model.Student;
import com.scholife1.repository.ActivityLogRepository;
import com.scholife1.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentApiController {

    @Autowired private StudentRepository     studentRepository;
    @Autowired private ActivityLogRepository activityLogRepository;

    @GetMapping
    public ResponseEntity<List<Student>> getAll() {
        return ResponseEntity.ok(studentRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        return studentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> count() {
        return ResponseEntity.ok(Map.of("total", studentRepository.count()));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Student student) {
        try {
            if (studentRepository.existsByStudentId(student.getStudentId()))
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("message", "Student ID already exists."));
            if (studentRepository.existsByEmail(student.getEmail()))
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("message", "Email already registered."));
            if (student.getStatus() == null)
                student.setStatus(Student.Status.ACTIVE);

            Student saved = studentRepository.save(student);
            log("Student enrolled",
                    saved.getFirstName() + " " + saved.getLastName() + " (" + saved.getStudentId() + ")",
                    ActivityLog.Category.STUDENTS, ActivityLog.LogStatus.SUCCESS);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "message",   "Student registered successfully.",
                    "studentId", saved.getId(),
                    "name",      saved.getFirstName() + " " + saved.getLastName()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Student updated) {
        return studentRepository.findById(id).map(s -> {
            if (updated.getFirstName() != null) s.setFirstName(updated.getFirstName());
            if (updated.getLastName()  != null) s.setLastName(updated.getLastName());
            if (updated.getEmail()     != null) s.setEmail(updated.getEmail());
            if (updated.getProgram()   != null) s.setProgram(updated.getProgram());
            if (updated.getYearLevel() != null) s.setYearLevel(updated.getYearLevel());
            if (updated.getStatus()    != null) s.setStatus(updated.getStatus());
            Student saved = studentRepository.save(s);
            log("Student updated", saved.getFirstName() + " " + saved.getLastName(),
                    ActivityLog.Category.STUDENTS, ActivityLog.LogStatus.SUCCESS);
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> toggleStatus(@PathVariable Long id) {
        return studentRepository.findById(id).map(s -> {
            s.setStatus(s.getStatus() == Student.Status.ACTIVE
                    ? Student.Status.INACTIVE : Student.Status.ACTIVE);
            studentRepository.save(s);
            return ResponseEntity.ok(Map.of("status", s.getStatus().name()));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!studentRepository.existsById(id)) return ResponseEntity.notFound().build();
        studentRepository.deleteById(id);
        log("Student deleted", "ID: " + id,
                ActivityLog.Category.STUDENTS, ActivityLog.LogStatus.SUCCESS);
        return ResponseEntity.ok(Map.of("message", "Deleted."));
    }

    private void log(String event, String entity,
                     ActivityLog.Category category, ActivityLog.LogStatus status) {
        try {
            ActivityLog log = new ActivityLog();
            log.setEvent(event);
            log.setEntity(entity);
            log.setCategory(category);
            log.setStatus(status);
            log.setCreatedAt(LocalDateTime.now());
            activityLogRepository.save(log);
        } catch (Exception ignored) {}
    }
}