package com.busybrains.ecommerce.dto;

public class ProfileResponse {
    private final Long id;
    private final String username;
    private final String email;
    private final String role;
    private final String token;

    public ProfileResponse(Long id, String username, String email, String role) {
        this(id, username, email, role, null);
    }

    public ProfileResponse(Long id, String username, String email, String role, String token) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.token = token;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getToken() { return token; }
}
