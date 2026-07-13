package com.PhishingDetectionv2.auth.event;

import java.time.Instant;
import java.util.UUID;

public record UserRegisteredEvent(

        UUID userId,

        UUID organizationId,

        String email,

        String verificationToken,

        Instant registeredAt

) {
}
