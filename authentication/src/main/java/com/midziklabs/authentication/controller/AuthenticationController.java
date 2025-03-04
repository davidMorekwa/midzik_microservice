package com.midziklabs.authentication.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    @GetMapping("/welcome")
    public String getWelcomePage() {
        return "Hello Weorld ffrom AuthenticationController";
    }
    @GetMapping("/user")
    public String getUser() {
        return "Hello User";
    }

    @GetMapping("/admin")
    public String getAdmin() {
        return "Hello Admin";
    }
    
    
    
}
