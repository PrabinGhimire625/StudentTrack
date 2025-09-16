package com.example.student_track.repository;

import com.example.student_track.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Add this method so AttendanceService can find student by QR
    Optional<Student> findByQrCode(String qrCode);
}
