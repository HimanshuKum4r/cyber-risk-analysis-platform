package com.PhishingDetectionv2.auth.service;

import com.PhishingDetectionv2.auth.dto.request.*;
import com.PhishingDetectionv2.auth.dto.response.JwtResponse;
import com.PhishingDetectionv2.auth.dto.response.RegisterResponse;
import com.PhishingDetectionv2.auth.entity.*;
import com.PhishingDetectionv2.auth.event.UserRegisteredEvent;
import com.PhishingDetectionv2.auth.repository.*;
import com.PhishingDetectionv2.auth.security.CustomUserDetails;
import com.PhishingDetectionv2.common.exception.DuplicateResourceException;
import com.PhishingDetectionv2.common.exception.InvalidRequestException;
import com.PhishingDetectionv2.common.exception.ResourceNotFoundException;
import com.PhishingDetectionv2.common.exception.UnauthorizedException;
import com.PhishingDetectionv2.organization.entity.Organization;
import com.PhishingDetectionv2.organization.entity.OrganizationStatus;
import com.PhishingDetectionv2.organization.entity.OrganizationType;
import com.PhishingDetectionv2.organization.entity.SubscriptionPlan;
import com.PhishingDetectionv2.organization.repository.OrganizationRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

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

    private final ApplicationEventPublisher applicationEventPublisher;

    private final RefreshTokenService refreshTokenService;

    private final UserMapper userMapper;

    // Email service (later)
    // private final EmailService emailService;
    @Override
    public RegisterResponse registerIndividual(RegisterRequest request) {

        validateRegistration(request);

        Organization organization =
                createOrganization(
                        request.getFirstName() + "'s Workspace",
                        OrganizationType.INDIVIDUAL
                );

        User user =
                createUser(request, organization);

        assignDefaultRole(user,"OWNER");


        VerificationToken verificationToken = createVerificationToken(user);



       publishRegistrationEvent(user,verificationToken);

        return buildRegisterResponse(user);
    }
    @Override
    public RegisterResponse registerOrganization(RegisterOrganizationRequest request) {
        validateRegistration(request);

        Organization organization = createOrganization(request.getOrganizationName(),OrganizationType.ORGANIZATION);

        User user = createOrganizationAdmin(request,organization);

        assignDefaultRole(user,"ADMIN");

        VerificationToken verificationToken = createVerificationToken(user);

        publishRegistrationEvent(user,verificationToken);

        return buildRegisterResponse(user);

    }
    @Override
    public JwtResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(

                        request.getEmail(),

                        request.getPassword()

                ));

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        User user = loadUser(request.getEmail());

        validateUser(user);

        JwtTokens tokens = generateTokens(user);


        return JwtResponse.builder()

                .accessToken(tokens.accessToken())

                .refreshToken(tokens.refreshToken())

                .tokenType("Bearer")

                .expiresIn(accessTokenExpiration)

                .user(userMapper.toResponse(user))

                .build();


    }

    @Override
    public JwtResponse refreshToken(RefreshTokenRequest request) {
        return null;
    }

    @Override
    public void logout(LogoutRequest request) {

    }

    @Override
    public void verifyEmail(String token) {

    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {

    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {

    }
//    @Override
//    @Transactional
//    public JwtResponse refreshToken(RefreshTokenRequest request) {
//
//        RefreshToken refreshToken =
//                validateRefreshToken(request.getRefreshToken());
//
//        User user = refreshToken.getUser();
//
//        String accessToken = generateAccessToken(user);
//
//        return buildJwtResponse(
//                user,
//                accessToken,
//                refreshToken.getToken()
//        );
//    }
//
//    private JwtResponse buildJwtResponse(User user, String accessToken, String token) {
//    }
//
//    private String generateAccessToken(User user) {
//    }
//
//    private RefreshToken validateRefreshToken(@NotBlank String refreshToken) {
//    }

    //internal dto
    public record JwtTokens(

            String accessToken,

            String refreshToken

    ) {
    }
    private JwtTokens generateTokens(User user) {

        CustomUserDetails userDetails =
                new CustomUserDetails(user);

        String accessToken =
                jwtService.generateAccessToken(userDetails);

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        return new JwtTokens(

                accessToken,

                refreshToken.getToken()

        );

    }



    private User loadUser(String email) {

        return userRepository.findByEmail(email)

                .orElseThrow(() ->

                        new ResourceNotFoundException(

                                "User not found."

                        )

                );

    }


    private void validateUser(User user) {

        if (!user.isEmailVerified()) {

            throw new UnauthorizedException(
                    "Please verify your email."
            );

        }

        if (!user.isEnabled()) {

            throw new UnauthorizedException(
                    "Account is disabled."
            );

        }

        if (user.isAccountLocked()) {

            throw new UnauthorizedException(
                    "Account is locked."
            );

        }

    }







    private RegisterResponse buildRegisterResponse(User user) {
       RegisterResponse response =  RegisterResponse.builder()
                .message("Registration successful. Please verify your email.")
                .email(user.getEmail())
                .organizationName(user.getOrganization().getName())
                .verificationRequired(true)
                .build();
        return response;
    }


    private void  validateRegistration(RegisterRequest request){

           if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidRequestException("Passwords do not match.");
        }

           if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "User already exists with email: " + request.getEmail()
            );
        }
    }
    private void validateRegistration(
            RegisterOrganizationRequest request) {

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidRequestException("Passwords do not match.");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "Email already registered."
            );
        }

        if (organizationRepository.existsByName(
                request.getOrganizationName())) {

            throw new DuplicateResourceException(
                    "Organization already exists."
            );
        }

    }
    private Organization createOrganization(
            String name,
            OrganizationType type
    )
    {
        Organization organization = Organization.builder()
                .name(name)
                .type(type)
                .status(OrganizationStatus.ACTIVE)
                .subscriptionPlan(SubscriptionPlan.FREE)
                .build();

        organizationRepository.save(organization);
        return organization;

    }

    private User createUser(RegisterRequest request, Organization organization){
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
        return user;
    }
    private User createOrganizationAdmin(
            RegisterOrganizationRequest request,
            Organization organization
    ) {

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

        return userRepository.save(user);

    }
    private void assignDefaultRole(User user, String defaultrole){
        Role ownerRole = roleRepository.findByName(defaultrole)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Default OWNER role not found"));


        UserRole userRole = UserRole.builder()
                .user(user)
                .role(ownerRole)
                .build();
        userRoleRepository.save(userRole);
    }

    private  VerificationToken createVerificationToken(User user){
        VerificationToken verificationToken = VerificationToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiresAt(Instant.now().plus(24, ChronoUnit.HOURS))
                .used(false)
                .build();


        return verificationTokenRepository.save(verificationToken);
    }
    private void publishRegistrationEvent(
            User user,
            VerificationToken verificationToken
    ) {

        applicationEventPublisher.publishEvent(

                new UserRegisteredEvent(

                        user.getId(),

                        user.getOrganization().getId(),

                        user.getEmail(),

                        verificationToken.getToken(),

                        Instant.now()

                )

        );

    }


}
