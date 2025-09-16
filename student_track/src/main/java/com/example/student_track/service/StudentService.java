package com.example.student_track.service;

import com.example.student_track.model.Student;
import com.example.student_track.repository.StudentRepository;
import com.example.student_track.util.QRCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    private final String uploadDir = "uploads/qr"; // folder to save QR images

    public Student addStudent(Student student) throws Exception {
        // Generate unique QR string
        String qrCodeString = UUID.randomUUID().toString();
        student.setQrCode(qrCodeString);

        // Generate QR image and save to backend folder
        String qrPath = QRCodeGenerator.generateQRCodeFile(qrCodeString, uploadDir);
        student.setQrCodeImagePath(qrPath);

        // Set timestamps
        student.setCreatedAt(java.time.LocalDateTime.now());
        student.setUpdatedAt(java.time.LocalDateTime.now());

        return studentRepository.save(student);
    }

    // Get all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Student updateStudent(Long id, Student updated) throws Exception {
        return studentRepository.findById(id).map(student -> {
            student.setName(updated.getName());
            student.setGrade(updated.getGrade());
            student.setSection(updated.getSection());
            student.setUpdatedAt(java.time.LocalDateTime.now());
            return studentRepository.save(student);
        }).orElseThrow(() -> new Exception("Student not found with ID: " + id));
    }

    public void deleteStudent(Long id) throws Exception {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new Exception("Student not found with ID: " + id));
        studentRepository.delete(student);
    }

}
