package com.example.exception.handler;

import com.example.exception.domain.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    public CustomAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException exception)
            throws IOException {

        String message = exception != null
                ? exception.getMessage()
                : "Access Denied: You don't have permission";

        HttpResponse httpResponse = new HttpResponse(
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN,
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                message
        );

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), httpResponse);
    }
}

