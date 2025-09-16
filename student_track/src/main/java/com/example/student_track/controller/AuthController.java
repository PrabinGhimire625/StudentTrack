package com.example.student_track.controller;

import com.example.student_track.dto.LoginRequest;
import com.example.student_track.dto.RegisterRequest;
import com.example.student_track.dto.UserResponse;
import com.example.student_track.model.User;
import com.example.student_track.service.UserService;
import com.example.student_track.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody RegisterRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userService.register(request.getUsername(), request.getEmail(), request.getPassword(),
                    request.getRole());
            response.put("success", true);
            response.put("message", "User registered successfully");
            response.put("data", new UserResponse(user.getUsername(), user.getEmail(), user.getRole()));
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginRequest request) {
        Map<String, Object> response = new HashMap<>();
        boolean success = userService.login(request.getEmail(), request.getPassword());

        if (success) {
            String token = JwtUtil.generateToken(request.getEmail());
            response.put("success", true);
            response.put("message", "Login successful!");
            response.put("token", token);
        } else {
            response.put("success", false);
            response.put("message", "Invalid email or password!");
        }
        return response;
    }

    @GetMapping("/profile")
    public Map<String, Object> getProfile(@RequestHeader("Authorization") String authHeader) {
        Map<String, Object> response = new HashMap<>();
        try {
            String token = authHeader;

            // Check if header starts with "Bearer "
            if (authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7); // remove "Bearer "
            }

            // Get email from JWT
            String email = JwtUtil.getEmailFromToken(token);

            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            response.put("success", true);
            response.put("data", new UserResponse(user.getUsername(), user.getEmail(), user.getRole()));
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }


    // Fetch all users 
    @GetMapping("/users")
    public Map<String, Object> getAllUsers(@RequestHeader("Authorization") String authHeader) {
        Map<String, Object> response = new HashMap<>();
        try {
            String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
            String email = JwtUtil.getEmailFromToken(token);

            User currentUser = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!"admin".equalsIgnoreCase(currentUser.getRole())) {
                throw new RuntimeException("Access denied: Only admins can view all users");
            }

            List<User> users = userService.findAll();
            List<UserResponse> userResponses = users.stream()
                    .map(u -> new UserResponse(u.getUsername(), u.getEmail(), u.getRole()))
                    .toList();

            response.put("success", true);
            response.put("data", userResponses);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }

    // Fetch single user by id
    @GetMapping("/users/{id}")
    public Map<String, Object> getUserById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userService.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            response.put("success", true);
            response.put("data", new UserResponse(user.getUsername(), user.getEmail(), user.getRole()));
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }

    // Update user by id
    @PutMapping("/users/{id}")
    public Map<String, Object> updateUser(@PathVariable Long id, @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            User updatedUser = userService.updateUser(id, request.get("username"), request.get("role"));
            response.put("success", true);
            response.put("message", "User updated successfully");
            response.put("data",
                    new UserResponse(updatedUser.getUsername(), updatedUser.getEmail(), updatedUser.getRole()));
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }

    // Delete user by id
    @DeleteMapping("/users/{id}")
    public Map<String, Object> deleteUser(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            userService.deleteById(id);
            response.put("success", true);
            response.put("message", "User deleted successfully");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }
}
