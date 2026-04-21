package com.example.exception;

import org.springframework.security.access.AccessDeniedException;

public class JwtAccessDeniedException extends AccessDeniedException {
    public JwtAccessDeniedException(String message) {
        super(message);
    }

}
