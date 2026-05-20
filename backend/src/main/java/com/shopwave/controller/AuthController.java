package com.shopwave.controller;

import com.shopwave.model.User;
import com.shopwave.service.JwtService;
import com.shopwave.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        try {
            User user = userService.register(req.getUsername(), req.getPassword(), req.getName());
            String token = jwtService.generateToken(user.getUsername());
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "username", user.getUsername(),
                    "name", user.getName(),
                    "message", "Registration successful"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        try {
            User user = userService.authenticate(req.getUsername(), req.getPassword());
            String token = jwtService.generateToken(user.getUsername());
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "username", user.getUsername(),
                    "name", user.getName(),
                    "message", "Login successful"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String username = jwtService.extractUsername(token);
            return userService.findByUsername(username)
                    .map(u -> ResponseEntity.ok(Map.of("username", u.getUsername(), "name", u.getName())))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        }
    }

    @Data
    static class RegisterRequest {
        @NotBlank(message = "Name is required")
        private String name;

        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 30, message = "Username must be 3-30 characters")
        private String username;

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        private String password;
    }

    @Data
    static class LoginRequest {
        @NotBlank private String username;
        @NotBlank private String password;
    }
}
