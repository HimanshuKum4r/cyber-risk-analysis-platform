package com.PhishingDetectionv2.auth.email;

import com.PhishingDetectionv2.auth.entity.User;
import com.PhishingDetectionv2.auth.entity.VerificationToken;
import com.PhishingDetectionv2.common.exception.EmailDeliveryException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final EmailTemplateBuilder emailTemplateBuilder;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Override
    public void sendVerificationEmail(
            String firstName,
            String email,
            String verificationToken
    ) {

        String verificationUrl =
                frontendUrl +
                        "/verify-email?token=" +
                        verificationToken;

        String html = emailTemplateBuilder.buildVerificationEmail(firstName,verificationUrl);

        sendEmail(html,email,"Verify your email", "Failed to send verification email.");


    }

    @Override
    public void sendPasswordResetEmail(
            String firstName,
            String email,
            String resetToken
    ) {

        String resetLink = frontendUrl +
                "/reset-password?token=" + resetToken;

        String html =
                emailTemplateBuilder.buildPasswordResetEmail(
                        firstName,
                        resetLink
                );

        sendEmail(html,email,"Reset your password", "Failed to send password reset email.");



    }

    @Override
    public void sendPasswordChangedEmail(
            String firstName,
            String email,
            Instant changedAt
    ) {

        String html = emailTemplateBuilder
                .buildPasswordChangedEmail(
                        firstName,
                        changedAt
                );
        sendEmail(html,email,"your password has been changed","failed to change your password");


    }


    private void  sendEmail(String htmlContent , String recipient ,String subject , String errorMessage){
        try {

            MimeMessage message =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);

            helper.setTo(recipient);

            helper.setSubject(subject);

            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (MessagingException | MailException ex) {
            log.error(
                    "Failed to send email to {}",
                    recipient,
                    ex
            );

            throw new EmailDeliveryException(errorMessage,ex);

        }

    }

}