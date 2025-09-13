package com.example.student_track.controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class LoginController {
    @GetMapping("/login")

    public String Login() {
        return "Hello from the login zone";
    }
}
