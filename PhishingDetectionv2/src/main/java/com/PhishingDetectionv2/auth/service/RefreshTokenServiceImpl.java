package com.PhishingDetectionv2.auth.service;

import com.PhishingDetectionv2.auth.entity.RefreshToken;
import com.PhishingDetectionv2.auth.entity.User;
import com.PhishingDetectionv2.auth.repository.RefreshTokenRepository;
import com.PhishingDetectionv2.common.exception.UnauthorizedException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
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

    @Override
    public RefreshToken validateRefreshToken(String token) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() ->
                        new UnauthorizedException("Invalid refresh token."));

        if (refreshToken.isRevoked()) {
            throw new UnauthorizedException("Refresh token has been revoked.");
        }

        if (refreshToken.isExpired()) {
            throw new UnauthorizedException("Refresh token has expired.");
        }

        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {

            refreshToken.setExpired(true);
            refreshTokenRepository.save(refreshToken);

            throw new UnauthorizedException("Refresh token has expired.");
        }

        return refreshToken;
    }

    @Override
    public RefreshToken rotateRefreshToken(RefreshToken oldToken) {

        oldToken.setRevoked(true);
        oldToken.setExpired(true);

        refreshTokenRepository.save(oldToken);

        return createRefreshToken(oldToken.getUser());
    }

    @Override
    public void revokeRefreshToken(RefreshToken refreshToken) {

        if (refreshToken.isRevoked()) {
            return;
        }
        refreshToken.setRevoked(true);
        refreshToken.setExpired(true);

        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public void revokeAllUserRefreshTokens(User user) {

        List<RefreshToken> refreshTokens =
                refreshTokenRepository.findByUser(user);

        refreshTokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });

        refreshTokenRepository.saveAll(refreshTokens);
    }
}
