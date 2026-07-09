package com.PhishingDetectionv2.auth.service;

import com.PhishingDetectionv2.auth.dto.request.RegisterRequest;
import com.PhishingDetectionv2.auth.dto.response.RegisterResponse;
import com.PhishingDetectionv2.auth.entity.Role;
import com.PhishingDetectionv2.auth.entity.User;
import com.PhishingDetectionv2.auth.entity.UserRole;
import com.PhishingDetectionv2.auth.entity.VerificationToken;
import com.PhishingDetectionv2.auth.repository.*;
import com.PhishingDetectionv2.organization.entity.Organization;
import com.PhishingDetectionv2.organization.entity.OrganizationStatus;
import com.PhishingDetectionv2.organization.entity.OrganizationType;
import com.PhishingDetectionv2.organization.entity.SubscriptionPlan;
import com.PhishingDetectionv2.organization.repository.OrganizationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final OrganizationRepository organizationRepository;

    private final RoleRepository roleRepository;

    private final UserRoleRepository userRoleRepository;

    private final VerificationTokenRepository verificationTokenRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;

    // Email service (later)
    // private final EmailService emailService;
    @Override
    public RegisterResponse registerIndividual(RegisterRequest request) {

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidRequestException("Passwords do not match.");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "User already exists with email: " + request.getEmail()
            );
        }

        Organization organization = Organization.builder()
                .name(request.getFirstName() + "'s Workspace")
                .type(OrganizationType.INDIVIDUAL)
                .status(OrganizationStatus.ACTIVE)
                .subscriptionPlan(SubscriptionPlan.FREE)
                .build();

        organizationRepository.save(organization);


        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .organization(organization)
                .enabled(false)
                .emailVerified(false)
                .accountLocked(false)
                .build();

        userRepository.save(user);

        Role ownerRole = roleRepository.findByName("OWNER")
                .orElseThrow(() ->
                        new ResourceNotFoundException("Default OWNER role not found"));


        UserRole userRole = UserRole.builder()
                .user(user)
                .role(ownerRole)
                .build();

        userRoleRepository.save(userRole);

        VerificationToken verificationToken = VerificationToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiresAt(Instant.now().plus(24, ChronoUnit.HOURS))
                .used(false)
                .build();

        verificationTokenRepository.save(verificationToken);

        applicationEventPublisher.publishEvent(
                new UserRegisteredEvent(
                        user.getId(),
                        verificationToken.getToken()
                )
        );

        return RegisterResponse.builder()
                .message("Registration successful. Please verify your email.")
                .email(user.getEmail())
                .organizationName(organization.getName())
                .verificationRequired(true)
                .build();
    }
}
