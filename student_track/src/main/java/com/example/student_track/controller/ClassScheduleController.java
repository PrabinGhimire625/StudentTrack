package com.example.student_track.controller;
import com.example.student_track.model.ClassSchedule;
import com.example.student_track.repository.ClassScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/class-schedule")
public class ClassScheduleController {

    @Autowired
    private ClassScheduleRepository classScheduleRepository;

    // Add a new class schedule
    @PostMapping("/add")
    public ResponseEntity<ClassSchedule> addClassSchedule(@RequestBody ClassSchedule schedule) {
        ClassSchedule saved = classScheduleRepository.save(schedule);
        return ResponseEntity.ok(saved);
    }

    // Get all schedules
    @GetMapping("/all")
    public ResponseEntity<?> getAllSchedules() {
        return ResponseEntity.ok(classScheduleRepository.findAll());
    }
}

