package com.PhishingDetectionv2.auth.service;

import com.PhishingDetectionv2.auth.entity.RefreshToken;
import com.PhishingDetectionv2.auth.entity.User;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(User user);
}
