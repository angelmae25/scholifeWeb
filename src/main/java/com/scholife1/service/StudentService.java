// FILE PATH: src/main/java/com/scholife1/service/StudentService.java

package com.scholife1.service;

import com.scholife1.model.Student;
import com.scholife1.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    public Student save(Student student) {
        return studentRepository.save(student);
    }

    public void delete(Long id) {
        studentRepository.deleteById(id);
    }

    public Student updateStatus(Long id, Student.Status newStatus) {
        Student s = getById(id);
        s.setStatus(newStatus);
        return studentRepository.save(s);
    }
}