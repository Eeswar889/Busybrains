package com.busybrains.ecommerce.service;

import com.busybrains.ecommerce.dto.ChangePasswordRequest;
import com.busybrains.ecommerce.dto.ProfileResponse;
import com.busybrains.ecommerce.dto.ProfileUpdateRequest;
import com.busybrains.ecommerce.model.User;
import com.busybrains.ecommerce.repository.UserRepository;
import com.busybrains.ecommerce.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public ProfileService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public ProfileResponse getProfile(String username) {
        return toResponse(loadUser(username));
    }

    public ProfileResponse updateProfile(String currentUsername, ProfileUpdateRequest request) {
        User user = loadUser(currentUsername);
        if (!currentUsername.equals(request.getUsername()) && userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(
                org.springframework.security.core.userdetails.User.withUsername(savedUser.getUsername())
                        .password(savedUser.getPassword())
                        .authorities(savedUser.getRole().name())
                        .build()
        );
        return new ProfileResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getRole().name(), token);
    }

    public void changePassword(String username, ChangePasswordRequest request) {
        User user = loadUser(username);
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    private User loadUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private ProfileResponse toResponse(User user) {
        return new ProfileResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole().name());
    }
}
