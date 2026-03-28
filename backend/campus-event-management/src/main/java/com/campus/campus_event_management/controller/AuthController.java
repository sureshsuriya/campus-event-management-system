package com.campus.campus_event_management.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.campus.campus_event_management.model.User;
import com.campus.campus_event_management.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // ==========================
    // REGISTER
    // ==========================
    @PostMapping("/register")
    public User register(@RequestBody User user) {

        Optional<User> existing = userRepository.findByEmail(user.getEmail());

        if (existing.isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // 🔥 Always store role in uppercase
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("STUDENT");
        } else {
            user.setRole(user.getRole().toUpperCase());
        }

        return userRepository.save(user);
    }

    // ==========================
    // LOGIN
    // ==========================
    @PostMapping("/login")
    public User login(@RequestBody User loginUser) {

        Optional<User> optionalUser =
                userRepository.findByEmail(loginUser.getEmail());

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Invalid email or password");
        }

        User user = optionalUser.get();

        if (!user.getPassword().equals(loginUser.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // 🔥 Validate role matches selected role
        if (!user.getRole().equalsIgnoreCase(loginUser.getRole())) {
            throw new RuntimeException("Role mismatch. Select correct role.");
        }

        user.setRole(user.getRole().toUpperCase());

        return user;
    }
}