package com.PhishingDetectionv2.auth.event;

import java.time.Instant;
import java.util.UUID;

public record PasswordChangedEvent(

        UUID userId,

        String firstName,

        String email,

        Instant changedAt

) {
}
