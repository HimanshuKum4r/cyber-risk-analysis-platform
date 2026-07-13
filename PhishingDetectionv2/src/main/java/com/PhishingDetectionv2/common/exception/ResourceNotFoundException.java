package com.PhishingDetectionv2.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class ResourceNotFoundException extends ApiException {
    public ResourceNotFoundException(String message) {
    super(message, HttpStatus.NOT_FOUND);
}
}
