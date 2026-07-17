package com.PhishingDetectionv2.auth.service;

import com.PhishingDetectionv2.auth.entity.PasswordResetToken;
import com.PhishingDetectionv2.auth.entity.User;
import com.PhishingDetectionv2.auth.repository.PasswordResetTokenRepository;
import com.PhishingDetectionv2.common.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService{

    @Value("${jwt.password-reset-token-expiration}")
    private long passwordResetTokenExpiration;
    private final PasswordResetTokenRepository passwordResetTokenRepository;


    @Override
    public PasswordResetToken createPasswordResetToken(User user) {

        PasswordResetToken token = PasswordResetToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiresAt(
                        Instant.now().plus(
                                passwordResetTokenExpiration,
                                ChronoUnit.MILLIS
                        )
                )
                .used(false)
                .build();

        return passwordResetTokenRepository.save(token);
    }

    @Override
    public PasswordResetToken validatePasswordResetToken(String token) {

        PasswordResetToken resetToken =
                passwordResetTokenRepository.findByToken(token)
                        .orElseThrow(() ->
                                new UnauthorizedException(
                                        "Invalid password reset token."
                                ));

        if (resetToken.isUsed()) {
            throw new UnauthorizedException(
                    "Password reset token has already been used."
            );
        }

        if (resetToken.getExpiresAt().isBefore(Instant.now())) {

            passwordResetTokenRepository.delete(resetToken);

            throw new UnauthorizedException(
                    "Password reset token has expired."
            );
        }

        return resetToken;
    }

    @Override
    public void markTokenAsUsed(PasswordResetToken token) {

        token.setUsed(true);

        passwordResetTokenRepository.save(token);
    }

    @Override
    public void deletePasswordResetToken(
            PasswordResetToken token
    ) {

        passwordResetTokenRepository.delete(token);

    }
}
