package com.PhishingDetectionv2.auth.service;

import com.PhishingDetectionv2.auth.entity.RefreshToken;
import com.PhishingDetectionv2.auth.entity.User;
import com.PhishingDetectionv2.auth.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken createRefreshToken(User user) {

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiresAt(
                        Instant.now().plus(
                                refreshTokenExpiration,
                                ChronoUnit.MILLIS
                        )
                )
                .revoked(false)
                .deviceName("Unknown Device")
                .ipAddress("Unknown")
                .userAgent("Unknown")
                .build();

        return refreshTokenRepository.save(refreshToken);
    }
}
