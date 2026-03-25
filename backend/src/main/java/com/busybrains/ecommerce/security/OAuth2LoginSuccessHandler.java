package com.busybrains.ecommerce.security;

import com.busybrains.ecommerce.model.Role;
import com.busybrains.ecommerce.model.User;
import com.busybrains.ecommerce.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public OAuth2LoginSuccessHandler(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        org.springframework.security.core.Authentication authentication) throws IOException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");
        String username = email != null ? email.split("@")[0] : "google_user_" + UUID.randomUUID();

        User user = userRepository.findByEmail(email).orElseGet(() -> createOauthUser(username, email));
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), List.of(new SimpleGrantedAuthority(user.getRole().name())));

        response.sendRedirect(frontendUrl + "/oauth-success?token=" + jwtService.generateToken(userDetails));
    }

    private User createOauthUser(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email == null ? username + "@oauth.local" : email);
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        user.setRole(Role.ROLE_USER);
        return userRepository.save(user);
    }
}
