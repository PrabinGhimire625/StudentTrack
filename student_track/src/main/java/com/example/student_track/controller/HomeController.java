package com.example.student_track.controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class HomeController {
    @GetMapping("/")

    public String home() {
        return "Welcome to Student Attendance & Result Management System!";
    }
}
