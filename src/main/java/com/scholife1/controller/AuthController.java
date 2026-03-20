// FILE PATH: src/main/java/com/scholife1/controller/AuthController.java

package com.scholife1.controller;

import com.scholife1.model.Admin;
import com.scholife1.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("admin", new Admin());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Admin admin, Model model) {
        try {
            adminService.register(admin);
            return "redirect:/auth/login?registered";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("admin", admin);
            return "auth/register";
        }
    }
}