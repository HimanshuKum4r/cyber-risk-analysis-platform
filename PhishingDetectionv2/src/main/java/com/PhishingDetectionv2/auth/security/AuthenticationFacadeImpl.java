package com.PhishingDetectionv2.auth.security;

import com.PhishingDetectionv2.auth.entity.User;
import com.PhishingDetectionv2.auth.repository.UserRepository;
import com.PhishingDetectionv2.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    private final UserRepository userRepository;

    @Override
    public User getCurrentUser() {

        CustomUserDetails userDetails = getCurrentUserDetails();

        return userRepository.findById(userDetails.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));
    }

    @Override
    public CustomUserDetails getCurrentUserDetails() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return (CustomUserDetails) authentication.getPrincipal();
    }
}