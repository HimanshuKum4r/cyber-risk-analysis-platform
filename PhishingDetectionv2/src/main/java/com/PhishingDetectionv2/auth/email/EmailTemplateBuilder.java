package com.PhishingDetectionv2.auth.email;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class EmailTemplateBuilder {

    public String buildVerificationEmail(
            String firstName,
            String verificationUrl
    ) {

        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <title>Verify Email</title>
                </head>

                <body style="font-family:Arial,sans-serif;background:#f4f4f4;padding:40px;">

                    <div style="
                            max-width:600px;
                            margin:auto;
                            background:white;
                            padding:40px;
                            border-radius:10px;
                            box-shadow:0 2px 8px rgba(0,0,0,.1);
                    ">

                        <h2 style="color:#2563eb;">
                            Welcome to PhishingDetection
                        </h2>

                        <p>Hello <strong>%s</strong>,</p>

                        <p>
                            Thank you for registering.
                            Please verify your email address.
                        </p>

                        <div style="margin:30px 0;">

                            <a href="%s"
                               style="
                                    background:#2563eb;
                                    color:white;
                                    text-decoration:none;
                                    padding:14px 28px;
                                    border-radius:6px;
                                    display:inline-block;
                               ">
                                Verify Email
                            </a>

                        </div>

                        <p>
                            This verification link expires in
                            <strong>24 hours</strong>.
                        </p>

                        <hr>

                        <small style="color:gray;">
                            If you didn't create this account,
                            you can safely ignore this email.
                        </small>

                    </div>

                </body>
                </html>
                """
                .formatted(firstName, verificationUrl);
    }


    public String buildPasswordResetEmail(
            String firstName,
            String resetLink
    ) {


        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Reset Your Password</title>
            </head>
            <body style="margin:0;padding:0;background:#f5f7fb;font-family:Arial,Helvetica,sans-serif;">

            <table width="100%%" cellpadding="0" cellspacing="0" style="padding:40px 0;">
                <tr>
                    <td align="center">

                        <table width="600" cellpadding="0" cellspacing="0"
                               style="background:#ffffff;border-radius:12px;padding:40px;
                               box-shadow:0 4px 12px rgba(0,0,0,0.08);">

                            <tr>
                                <td align="center">
                                    <h1 style="color:#2563eb;margin:0;">
                                        PhishingDetection
                                    </h1>
                                </td>
                            </tr>

                            <tr>
                                <td style="padding-top:30px;">
                                    <h2 style="margin:0;color:#111827;">
                                        Reset Your Password
                                    </h2>
                                </td>
                            </tr>

                            <tr>
                                <td style="padding-top:20px;
                                           color:#4b5563;
                                           font-size:16px;
                                           line-height:26px;">

                                    Hi <strong>%s</strong>,

                                    <br><br>

                                    We received a request to reset the password for your
                                    PhishingDetection account.

                                    <br><br>

                                    Click the button below to choose a new password.

                                </td>
                            </tr>

                            <tr>
                                <td align="center" style="padding:35px 0;">

                                    <a href="%s"
                                       style="
                                        background:#2563eb;
                                        color:#ffffff;
                                        text-decoration:none;
                                        padding:14px 32px;
                                        border-radius:8px;
                                        display:inline-block;
                                        font-weight:bold;
                                       ">
                                        Reset Password
                                    </a>

                                </td>
                            </tr>

                            <tr>
                                <td style="
                                        color:#6b7280;
                                        font-size:14px;
                                        line-height:24px;">

                                    This password reset link will expire in
                                    <strong>15 minutes</strong>.

                                    <br><br>

                                    If you didn't request a password reset,
                                    you can safely ignore this email.
                                    Your password will remain unchanged.

                                </td>
                            </tr>

                            <tr>
                                <td style="padding-top:30px;">

                                    <hr style="border:none;border-top:1px solid #e5e7eb;">

                                </td>
                            </tr>

                            <tr>
                                <td style="
                                        color:#9ca3af;
                                        font-size:12px;
                                        line-height:20px;
                                        padding-top:15px;">

                                    If the button above doesn't work,
                                    copy and paste the following link into your browser:

                                    <br><br>

                                    <a href="%s">%s</a>

                                </td>
                            </tr>

                        </table>

                    </td>
                </tr>
            </table>

            </body>
            </html>
            """.formatted(
                firstName,
                resetLink,
                resetLink,
                resetLink
        );
    }
    public String buildPasswordChangedEmail(
            String firstName,
            Instant changedAt
    ) {

        return """
            <!DOCTYPE html>
            <html>
            <body style="font-family:Arial,sans-serif;background:#f5f7fb;padding:40px;">

                <div style="
                        max-width:600px;
                        margin:auto;
                        background:white;
                        border-radius:10px;
                        padding:40px;
                        box-shadow:0 2px 10px rgba(0,0,0,.08);
                ">

                    <h2 style="color:#2563eb;">
                        Password Changed Successfully
                    </h2>

                    <p>Hi <strong>%s</strong>,</p>

                    <p>
                        This email confirms that the password for your
                        <strong>PhishingDetection</strong> account has been
                        successfully changed.
                    </p>

                    <p>
                        <strong>Changed At:</strong> %s
                    </p>

                    <p>
                        If you made this change, you can safely ignore this email.
                    </p>

                    <p style="color:#dc2626;font-weight:bold;">
                        If you did NOT change your password,
                        please contact support immediately and secure your account.
                    </p>

                    <hr>

                    <p style="font-size:12px;color:#777;">
                        This is an automated security notification.
                    </p>

                </div>

            </body>
            </html>
            """.formatted(
                firstName,
                changedAt
        );
    }

}