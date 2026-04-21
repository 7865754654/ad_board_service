package com.example.exception;

import org.springframework.security.core.AuthenticationException;

//посмотреть что это
public class JwtAuthenticationException extends AuthenticationException{

    public JwtAuthenticationException(String message) {
        super(message);
    }


}
