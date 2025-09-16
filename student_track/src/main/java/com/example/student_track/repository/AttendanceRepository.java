package com.example.student_track.repository;

import com.example.student_track.model.Attendance;
import com.example.student_track.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByStudentAndDate(Student student, LocalDate date);

    List<Attendance> findByDate(LocalDate date);
}
