package com.PhishingDetectionv2.auth.service;

import com.PhishingDetectionv2.auth.entity.User;
import com.PhishingDetectionv2.auth.entity.VerificationToken;

public interface VerificationTokenService {

    VerificationToken createVerificationToken(User user);

    VerificationToken validateVerificationToken(String token);

    void deleteVerificationToken(VerificationToken token);

}
