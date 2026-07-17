package com.PhishingDetectionv2.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class EmailDeliveryException extends ApiException{
    public EmailDeliveryException(String message) {
        super(message, HttpStatus.SERVICE_UNAVAILABLE);
    }
    public EmailDeliveryException(String message, Throwable cause) {
        super(message, HttpStatus.SERVICE_UNAVAILABLE, cause);
    }
}
