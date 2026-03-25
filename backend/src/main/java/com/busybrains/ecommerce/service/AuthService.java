package com.busybrains.ecommerce.service;

import com.busybrains.ecommerce.dto.AuthRequest;
import com.busybrains.ecommerce.dto.AuthResponse;
import com.busybrains.ecommerce.dto.RegisterRequest;
import com.busybrains.ecommerce.model.Role;
import com.busybrains.ecommerce.model.User;
import com.busybrains.ecommerce.repository.UserRepository;
import com.busybrains.ecommerce.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);

        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRole().name())
                .build();
        return new AuthResponse(jwtService.generateToken(userDetails), user.getUsername(), user.getRole().name());
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRole().name())
                .build();
        return new AuthResponse(jwtService.generateToken(userDetails), user.getUsername(), user.getRole().name());
    }
}
