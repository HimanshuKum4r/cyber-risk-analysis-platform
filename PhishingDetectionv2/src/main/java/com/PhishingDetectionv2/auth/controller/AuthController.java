package com.PhishingDetectionv2.auth.controller;

import com.PhishingDetectionv2.auth.dto.request.*;
import com.PhishingDetectionv2.auth.dto.response.JwtResponse;
import com.PhishingDetectionv2.auth.dto.response.RegisterResponse;
import com.PhishingDetectionv2.auth.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register/individual")
    public ResponseEntity<RegisterResponse> registerIndividual(
            @Valid @RequestBody RegisterRequest request
    ) {

        RegisterResponse response =
                authenticationService.registerIndividual(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/register/organization")
    public ResponseEntity<RegisterResponse> registerOrganization(
            @Valid @RequestBody RegisterOrganizationRequest request
    ) {

        RegisterResponse response =
                authenticationService.registerOrganization(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {

        JwtResponse response =
                authenticationService.login(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request
    ) {

        JwtResponse response = authenticationService.refreshToken(
                request.getRefreshToken()
        );

        return ResponseEntity.ok(response);

    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @Valid @RequestBody LogoutRequest request
    ) {

        authenticationService.logout(
                request
        );

        return ResponseEntity.noContent().build();
    }
    @PostMapping("/logout-all")
    public ResponseEntity<Void> logoutAll() {

        authenticationService.logoutAll();

        return ResponseEntity.noContent().build();
    }
    @GetMapping("/verify-email")
    public ResponseEntity<Void> verifyEmail(
            @RequestParam String token
    ) {

        authenticationService.verifyEmail(token);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request
    ) {

        authenticationService.forgotPassword(request);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
    ) {

        authenticationService.resetPassword(request);

        return ResponseEntity.noContent().build();
    }


}