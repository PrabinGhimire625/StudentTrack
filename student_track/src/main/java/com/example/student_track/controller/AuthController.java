// package com.example.student_track.controller;
// import com.example.student_track.dto.LoginRequest;
// import com.example.student_track.dto.RegisterRequest;
// import com.example.student_track.model.User;
// import com.example.student_track.service.UserService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.*;

// @RestController
// @RequestMapping("/auth")
// public class AuthController {

//     @Autowired
//     private UserService userService;

//     @PostMapping("/register")
//     public String register(@RequestBody RegisterRequest request) {
//         try {
//             User user = userService.register(request.getUsername(), request.getEmail(), request.getPassword());
//             return "User register successfully: " + user.getUsername();
//         } catch (Exception e) {
//             return "Error: " + e.getMessage();
//         }
//     }

//     @PostMapping("/login")
//     public String login(@RequestBody LoginRequest request) {
//         boolean success = userService.login(request.getEmail(), request.getPassword());
//         return success ? "Login successful!" : "Invalid email or password!";
//     }
// }

// package com.example.student_track.controller;

package com.example.student_track.controller;

import com.example.student_track.dto.LoginRequest;
import com.example.student_track.dto.RegisterRequest;
import com.example.student_track.dto.UserResponse;
import com.example.student_track.model.User;
import com.example.student_track.service.UserService;
import com.example.student_track.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
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
            User user = userService.register(request.getUsername(), request.getEmail(), request.getPassword());
            response.put("success", true);
            response.put("message", "User registered successfully");
            response.put("data", new UserResponse(user.getUsername(), user.getEmail()));
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
}
