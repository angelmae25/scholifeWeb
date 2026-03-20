package com.scholife1.controller;

import com.scholife1.model.Student;
import com.scholife1.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("newStudent", new Student());
        return "dashboard/students";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute Student student) {
        studentService.save(student);
        return "redirect:/students";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        studentService.delete(id);
        return "redirect:/students";
    }
}