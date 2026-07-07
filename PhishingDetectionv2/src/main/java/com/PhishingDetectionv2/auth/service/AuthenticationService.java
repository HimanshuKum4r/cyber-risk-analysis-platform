package com.PhishingDetectionv2.auth.service;

import com.PhishingDetectionv2.auth.dto.request.*;
import com.PhishingDetectionv2.auth.dto.response.JwtResponse;
import com.PhishingDetectionv2.auth.dto.response.RegisterResponse;

public interface AuthenticationService {

    RegisterResponse register(RegisterRequest request);

    JwtResponse login(LoginRequest request);

    JwtResponse refreshToken(RefreshTokenRequest request);

    void logout(LogoutRequest request);

    void verifyEmail(String token);

    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

}
