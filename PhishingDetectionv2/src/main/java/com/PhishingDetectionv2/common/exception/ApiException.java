package com.PhishingDetectionv2.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public abstract class ApiException extends RuntimeException {

    private final HttpStatusCode statusCode;

    protected ApiException(String message, HttpStatusCode statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
    protected ApiException(
            String message,
            HttpStatusCode statusCode,
            Throwable cause
    ) {
        super(message, cause);
        this.statusCode = statusCode;
    }
}