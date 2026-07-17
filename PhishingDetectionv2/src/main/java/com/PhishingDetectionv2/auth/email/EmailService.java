package com.PhishingDetectionv2.auth.email;

import com.PhishingDetectionv2.auth.entity.User;
import com.PhishingDetectionv2.auth.entity.VerificationToken;

import java.time.Instant;

public interface EmailService {

    void sendVerificationEmail(
            String firstName,
            String email,
            String verificationToken
    );
    void sendPasswordResetEmail(
            String firstName,
            String email,
            String resetToken
    );
    void sendPasswordChangedEmail(
            String firstName,
            String email,
            Instant changedAt
    );

}