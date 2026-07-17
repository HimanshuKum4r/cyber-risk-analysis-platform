package com.PhishingDetectionv2.auth.listener;

import com.PhishingDetectionv2.auth.email.EmailService;
import com.PhishingDetectionv2.auth.event.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserRegistrationListener {

    private final EmailService emailService;

    @EventListener
    @Async
    public void handleUserRegistered(UserRegisteredEvent event) {

        log.info("Sending verification email to {}", event.email());

        emailService.sendVerificationEmail(
                event.firstName(),
                event.email(),
                event.verificationToken()
        );
    }
}