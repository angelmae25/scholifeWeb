package com.scholife1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    // DASHBOARD PAGES ONLY — huwag lagyan ng /auth/** dito
    @GetMapping("/dashboard/index")
    public String dashboard() { return "dashboard/index"; }

    @GetMapping("/dashboard/activity")
    public String activity() { return "dashboard/activity"; }

    @GetMapping("/dashboard/students")
    public String students() { return "dashboard/students"; }

    @GetMapping("/dashboard/organizations")
    public String organizations() { return "dashboard/organizations"; }

    @GetMapping("/dashboard/roles")
    public String roles() { return "dashboard/roles"; }

    @GetMapping("/dashboard/messages")
    public String messages() { return "dashboard/messages"; }

    @GetMapping("/dashboard/admins")
    public String admins() { return "dashboard/admins"; }
}