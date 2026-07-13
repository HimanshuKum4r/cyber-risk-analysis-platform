package com.PhishingDetectionv2.common.exception;

import org.springframework.http.HttpStatus;

public class TokenExpiredException extends ApiException{
    public TokenExpiredException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
