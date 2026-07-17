package com.PhishingDetectionv2.auth.service;

import com.PhishingDetectionv2.auth.entity.PasswordResetToken;
import com.PhishingDetectionv2.auth.entity.User;

public interface PasswordResetTokenService {

    PasswordResetToken createPasswordResetToken(User user);

    PasswordResetToken validatePasswordResetToken(String token);

    void markTokenAsUsed(PasswordResetToken token);

    void deletePasswordResetToken(PasswordResetToken token);

}
