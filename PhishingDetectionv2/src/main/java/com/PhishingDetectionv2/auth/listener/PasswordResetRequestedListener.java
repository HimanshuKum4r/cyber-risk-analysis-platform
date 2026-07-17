package com.PhishingDetectionv2.auth.listener;

import com.PhishingDetectionv2.auth.email.EmailService;
import com.PhishingDetectionv2.auth.event.PasswordResetRequestedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordResetRequestedListener {

    private final EmailService emailService;

    @Async
    @EventListener
    public void handlePasswordResetRequested(
            PasswordResetRequestedEvent event
    ) {

        log.info(
                "Sending password reset email to {}",
                event.email()
        );

        emailService.sendPasswordResetEmail(
                event.firstName(),
                event.email(),
                event.resetToken()
        );

        log.info(
                "Password reset email sent successfully to {}",
                event.email()
        );
    }
}
