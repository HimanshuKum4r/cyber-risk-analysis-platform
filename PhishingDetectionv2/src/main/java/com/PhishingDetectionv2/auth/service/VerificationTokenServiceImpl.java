package com.PhishingDetectionv2.auth.service;

import com.PhishingDetectionv2.auth.entity.User;
import com.PhishingDetectionv2.auth.entity.VerificationToken;
import com.PhishingDetectionv2.auth.repository.VerificationTokenRepository;
import com.PhishingDetectionv2.common.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    @Override
    public   VerificationToken createVerificationToken(User user){
        VerificationToken verificationToken = VerificationToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiresAt(Instant.now().plus(24, ChronoUnit.HOURS))
                .used(false)
                .build();


        return verificationTokenRepository.save(verificationToken);
    }

    @Override
    public VerificationToken validateVerificationToken(
            String token
    ) {

        VerificationToken verificationToken =
                verificationTokenRepository.findByToken(token)
                        .orElseThrow(() ->
                                new UnauthorizedException(
                                        "Invalid verification token."
                                ));

        if (verificationToken.getExpiresAt().isBefore(Instant.now())) {

            verificationTokenRepository.delete(verificationToken);

            throw new UnauthorizedException(
                    "Verification token has expired."
            );
        }

        return verificationToken;
    }

    @Override
    public void deleteVerificationToken(
            VerificationToken verificationToken
    ) {

        verificationTokenRepository.delete(verificationToken);

    }


}
