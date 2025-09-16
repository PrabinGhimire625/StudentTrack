package com.example.student_track.service;

import com.example.student_track.model.Attendance;
import com.example.student_track.model.ClassSchedule;
import com.example.student_track.model.Student;
import com.example.student_track.repository.AttendanceRepository;
import com.example.student_track.repository.ClassScheduleRepository;
import com.example.student_track.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private ClassScheduleRepository classScheduleRepository;

    // Mark attendance with time window check
    public String markAttendance(String qrCode) {
        Student student = studentRepository.findByQrCode(qrCode)
                .orElseThrow(() -> new RuntimeException("Invalid QR Code"));

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        // Check if class exists for this student today
       // grade/section matches AND the current time is within the start/end time window.
        List<ClassSchedule> schedules = classScheduleRepository.findByClassDate(today);
        boolean withinWindow = schedules.stream().anyMatch(s -> s.getGrade().equals(student.getGrade()) &&
                s.getSection().equals(student.getSection()) &&
                !now.isBefore(s.getStartTime()) &&
                !now.isAfter(s.getEndTime()));

        if (!withinWindow) {
            return "Attendance cannot be marked outside the class time window!";
        }

        // Prevent duplicate
        Optional<Attendance> existing = attendanceRepository.findByStudentAndDate(student, today);
        if (existing.isPresent()) {
            return "Attendance already marked for today!";
        }

        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setDate(today);
        attendance.setStatus("PRESENT");
        attendance.setMarkedAt(LocalDateTime.now());

        attendanceRepository.save(attendance);
        return "Attendance marked successfully for " + student.getName();
    }

    // mark ABSENT for students who did not scan
    public String finalizeAttendance() {
        LocalDate today = LocalDate.now();
        List<ClassSchedule> schedules = classScheduleRepository.findByClassDate(today);

        int count = 0;
        for (ClassSchedule schedule : schedules) {
            // Get all students in that class
            List<Student> students = studentRepository.findByGradeAndSection(schedule.getGrade(),
                    schedule.getSection());
            for (Student student : students) {
                boolean alreadyMarked = attendanceRepository.findByStudentAndDate(student, today).isPresent();
                if (!alreadyMarked) {
                    Attendance absent = new Attendance();
                    absent.setStudent(student);
                    absent.setDate(today);
                    absent.setStatus("ABSENT");
                    absent.setMarkedAt(LocalDateTime.now());
                    attendanceRepository.save(absent);
                    count++;
                }
            }
        }
        return count + " students marked as ABSENT for today.";
    }

    // Get all attendance records for today
    public List<Attendance> getAttendanceForToday() {
        LocalDate today = LocalDate.now();
        return attendanceRepository.findByDate(today);
    }
}
