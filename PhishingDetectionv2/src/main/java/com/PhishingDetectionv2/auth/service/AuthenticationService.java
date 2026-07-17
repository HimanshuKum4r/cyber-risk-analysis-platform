package com.PhishingDetectionv2.auth.service;

import com.PhishingDetectionv2.auth.dto.request.*;
import com.PhishingDetectionv2.auth.dto.response.JwtResponse;
import com.PhishingDetectionv2.auth.dto.response.RegisterResponse;

public interface AuthenticationService {

    RegisterResponse registerIndividual(RegisterRequest request);

    JwtResponse login(LoginRequest request);

    JwtResponse refreshToken(String request);

    void logout(LogoutRequest request);

    void verifyEmail(String token);

    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

    RegisterResponse registerOrganization(RegisterOrganizationRequest request);

    void logoutAll();

    void changePassword(ChangePasswordRequest request);
}
