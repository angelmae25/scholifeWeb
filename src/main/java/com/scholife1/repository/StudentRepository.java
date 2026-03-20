package com.scholife1.repository;

import com.scholife1.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentId(String studentId);
    Optional<Student> findByEmail(String email);
    List<Student> findByStatus(Student.Status status);
    List<Student> findByProgramContainingIgnoreCase(String program);
}