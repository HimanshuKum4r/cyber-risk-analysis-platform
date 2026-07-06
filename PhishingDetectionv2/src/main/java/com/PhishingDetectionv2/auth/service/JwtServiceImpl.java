package com.PhishingDetectionv2.auth.service;

import com.PhishingDetectionv2.auth.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService{

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;


    private SecretKey getSigningKey() {

        byte[] keyBytes = Decoders.BASE64.decode(secret);

        return Keys.hmacShaKeyFor(keyBytes);

    }

    @Override
    public String generateAccessToken(CustomUserDetails user) {
        return "";
    }

    @Override
    public String generateAccessToken(Map<String, Object> extraClaims, CustomUserDetails user) {
        return "";
    }

    @Override
    public String extractUsername(String token) {
        return "";
    }

    @Override
    public Claims extractAllClaims(String token) {
        return null;
    }

    @Override
    public boolean isTokenValid(String token, CustomUserDetails user) {
        return false;
    }

    @Override
    public boolean isTokenExpired(String token) {
        return false;
    }
}
