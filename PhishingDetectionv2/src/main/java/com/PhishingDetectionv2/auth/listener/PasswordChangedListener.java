package com.PhishingDetectionv2.auth.listener;

import com.PhishingDetectionv2.auth.email.EmailService;
import com.PhishingDetectionv2.auth.event.PasswordChangedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordChangedListener {

    private final EmailService emailService;

    @Async
    @EventListener
    public void handlePasswordChanged(
            PasswordChangedEvent event
    ) {

        log.info(
                "Sending password changed confirmation email to {}",
                event.email()
        );

        emailService.sendPasswordChangedEmail(
                event.firstName(),
                event.email(),
                event.changedAt()
        );

        log.info(
                "Password changed confirmation email sent to {}",
                event.email()
        );
    }
}