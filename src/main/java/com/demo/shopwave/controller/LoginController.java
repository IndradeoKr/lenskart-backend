package com.demo.shopwave.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.shopwave.dto.UserDTO;
import com.demo.shopwave.entity.User;
import com.demo.shopwave.repository.UserRepository;
import com.demo.shopwave.util.JwtUtil;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

@RestController
@RequestMapping
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public static class LoginRequest {
        public String email;
        public String password;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        User user = userRepository.findByEmail(req.email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid credentials"));
        }

        String stored = user.getPassword();
        boolean matches = false;
        try {
            matches = passwordEncoder.matches(req.password, stored);
        } catch (Exception e) {
            // ignore
        }
        if (!matches && !stored.equals(req.password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid credentials"));
        }

        // If stored password was plain but matched raw, re-hash and save
        if (!stored.startsWith("$2") && matches) {
            user.setPassword(passwordEncoder.encode(req.password));
            userRepository.save(user);
        }

        UserDTO dto = new UserDTO();
        dto.setUserid(user.getUserid());
        dto.setUserName(user.getUserName());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setAddress(user.getAddress());
        dto.setRole(user.getRole());

        // Generate JWT
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        dto.setToken(token);

        return ResponseEntity.ok(dto);
    }
}
