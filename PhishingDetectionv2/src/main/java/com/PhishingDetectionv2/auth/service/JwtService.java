package com.PhishingDetectionv2.auth.service;

import com.PhishingDetectionv2.auth.security.CustomUserDetails;
import io.jsonwebtoken.Claims;

import java.util.Map;

public interface JwtService {

    String generateAccessToken(CustomUserDetails user);

    String generateAccessToken(
            Map<String, Object> extraClaims,
            CustomUserDetails user
    );

    String extractUsername(String token);

    Claims extractAllClaims(String token);

    boolean isTokenValid(
            String token,
            CustomUserDetails user
    );

    boolean isTokenExpired(String token);

}
