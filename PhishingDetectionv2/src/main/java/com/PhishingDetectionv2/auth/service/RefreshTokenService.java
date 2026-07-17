package com.PhishingDetectionv2.auth.service;

import com.PhishingDetectionv2.auth.entity.RefreshToken;
import com.PhishingDetectionv2.auth.entity.User;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(User user);

    RefreshToken validateRefreshToken(String token);

    RefreshToken rotateRefreshToken(RefreshToken refreshToken);

    void revokeRefreshToken(RefreshToken refreshToken);

    void revokeAllUserRefreshTokens(User user);
}
