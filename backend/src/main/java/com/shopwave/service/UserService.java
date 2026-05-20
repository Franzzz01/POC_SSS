package com.shopwave.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.shopwave.model.User;
import com.shopwave.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private static final String USERS_FILE = "users.json";
    private static final TypeReference<List<User>> USER_LIST_TYPE = new TypeReference<>() {};

    private final FileRepository fileRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(String username, String password, String name) {
        // Check if username exists
        if (findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username '" + username + "' is already taken");
        }

        String passwordHash = passwordEncoder.encode(password);
        User user = new User(username.toLowerCase().trim(), name.trim(), passwordHash);

        fileRepository.save(USERS_FILE, USER_LIST_TYPE, user,
                u -> u.getUsername().equals(user.getUsername()), false);

        log.info("Registered new user: {}", username);
        return user;
    }

    public User authenticate(String username, String password) {
        User user = findByUsername(username.toLowerCase().trim())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        return user;
    }

    public Optional<User> findByUsername(String username) {
        return fileRepository.findOne(USERS_FILE, USER_LIST_TYPE,
                u -> u.getUsername().equalsIgnoreCase(username));
    }

    public List<User> findAll() {
        return fileRepository.readAll(USERS_FILE, USER_LIST_TYPE);
    }
}
