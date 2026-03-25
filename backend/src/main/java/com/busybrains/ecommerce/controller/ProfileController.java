package com.busybrains.ecommerce.controller;

import com.busybrains.ecommerce.dto.ChangePasswordRequest;
import com.busybrains.ecommerce.dto.ProfileResponse;
import com.busybrains.ecommerce.dto.ProfileUpdateRequest;
import com.busybrains.ecommerce.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ProfileResponse getProfile(Authentication authentication) {
        return profileService.getProfile(authentication.getName());
    }

    @PutMapping
    public ProfileResponse updateProfile(Authentication authentication, @Valid @RequestBody ProfileUpdateRequest request) {
        return profileService.updateProfile(authentication.getName(), request);
    }

    @PutMapping("/change-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(Authentication authentication, @Valid @RequestBody ChangePasswordRequest request) {
        profileService.changePassword(authentication.getName(), request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIllegalArgument(IllegalArgumentException ex) {
        return Map.of("message", ex.getMessage());
    }
}
