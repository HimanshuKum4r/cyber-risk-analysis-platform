package com.PhishingDetectionv2.auth.security;

import com.PhishingDetectionv2.auth.entity.User;

public interface AuthenticationFacade {

    User getCurrentUser();

    CustomUserDetails getCurrentUserDetails();

}