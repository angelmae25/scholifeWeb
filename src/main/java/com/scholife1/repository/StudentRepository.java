// FILE PATH: src/main/java/com/scholife1/repository/StudentRepository.java

package com.scholife1.repository;

import com.scholife1.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);
    Optional<Student> findByStudentId(String studentId);
    boolean existsByStudentId(String studentId);
    boolean existsByEmail(String email);
    long countByProgram(String program);
    long countByYearLevel(String yearLevel);
    long countByStatus(Student.Status status);
}