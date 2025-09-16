package com.example.student_track.repository;

import com.example.student_track.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByQrCode(String qrCode);
     List<Student> findByGradeAndSection(String grade, String section);
}
