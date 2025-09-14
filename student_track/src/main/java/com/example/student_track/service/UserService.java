package com.example.student_track.service;

import com.example.student_track.model.User;
import com.example.student_track.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User register(String username, String email, String password, String role) throws Exception {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new Exception("Username already exists!");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new Exception("Email already exists!");
        }

        if (role == null || (!role.equals("student") && !role.equals("teacher") && !role.equals("admin"))) {
            role = "student"; // fallback default
        }

        String hashedPassword = passwordEncoder.encode(password);
        User user = new User(username, email, hashedPassword, role);
        return userRepository.save(user);
    }

    public boolean login(String email, String password) {
        return userRepository.findByEmail(email)
                .map(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(false);
    }

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);

    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User updateUser(Long id, String username, String role) throws Exception {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new Exception("User not found"));

        if (username != null && !username.isEmpty()) {
            user.setUsername(username);
        }
        if (role != null && !role.isEmpty()) {
            user.setRole(role);
        }

        return userRepository.save(user);
    }

    public void deleteById(Long id) throws Exception {
        if (!userRepository.existsById(id)) {
            throw new Exception("User not found");
        }
        userRepository.deleteById(id);
    }
    
}
