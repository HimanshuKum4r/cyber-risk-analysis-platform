package com.PhishingDetectionv2.auth.event;

import java.time.Instant;
import java.util.UUID;

public record PasswordResetRequestedEvent(

        UUID userId,

        String firstName,

        String email,

        String resetToken,

        Instant requestedAt

) {}