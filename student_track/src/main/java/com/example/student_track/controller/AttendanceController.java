package com.example.student_track.controller;
import com.example.student_track.model.Attendance;
import com.example.student_track.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    // Mark attendance by scanning QR
    @PostMapping("/mark")
    public ResponseEntity<?> markAttendance(@RequestParam String qrCode) {
        try {
            String message = attendanceService.markAttendance(qrCode);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // Get today’s attendance list
    @GetMapping("/today")
    public ResponseEntity<List<Attendance>> getTodayAttendance() {
        return ResponseEntity.ok(attendanceService.getAttendanceForToday());
    }

     // Finalize attendance (mark ABSENT)
    @PostMapping("/finalize")
    public ResponseEntity<String> finalizeAttendance() {
        try {
            String message = attendanceService.finalizeAttendance();
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

}
