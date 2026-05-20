package com.shopwave.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String username;
    private String name;

    @JsonIgnore
    private String passwordHash;

    private LocalDateTime createdAt;
    private String role = "USER";

    public User(String username, String name, String passwordHash) {
        this.username = username;
        this.name = name;
        this.passwordHash = passwordHash;
        this.createdAt = LocalDateTime.now();
    }
}
