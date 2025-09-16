package com.example.student_track.service;

import com.example.student_track.model.Attendance;
import com.example.student_track.model.Student;
import com.example.student_track.repository.AttendanceRepository;
import com.example.student_track.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    // Mark attendance using QR code
    public String markAttendance(String qrCode) throws Exception {
        // Find student by QR code
        Optional<Student> studentOpt = studentRepository.findByQrCode(qrCode);
        if (studentOpt.isEmpty()) {
            throw new Exception("Invalid QR Code");
        }
        Student student = studentOpt.get();

        LocalDate today = LocalDate.now();

        // Check if attendance already marked
        Optional<Attendance> existing = attendanceRepository.findByStudentAndDate(student, today);
        if (existing.isPresent()) {
            return "Attendance already marked for today!";
        }

        // Create new attendance record
        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setDate(today);
        attendance.setStatus("PRESENT");
        attendance.setMarkedAt(LocalDateTime.now());

        attendanceRepository.save(attendance);

        return "Attendance marked successfully for " + student.getName();
    }

    // Get all attendance records for today
    public List<Attendance> getAttendanceForToday() {
        LocalDate today = LocalDate.now();
        return attendanceRepository.findByDate(today);
    }
}
