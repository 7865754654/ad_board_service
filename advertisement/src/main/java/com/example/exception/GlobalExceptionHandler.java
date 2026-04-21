package com.example.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

@ExceptionHandler(EntityNotFoundException.class)
public ResponseEntity<ExceptionResponseDto> handleEntityNotFound(EntityNotFoundException e) {
   log.info("Entity not found: ", e);
   ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
                "Ресурс не найден. Повторите попытку.",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exceptionResponseDto);
    }

@ExceptionHandler(UsernameNotFoundException.class)
public ResponseEntity<ExceptionResponseDto> handleUsernameNotFound(UsernameNotFoundException e) {
    log.warn("User already exists: ", e);

    ExceptionResponseDto exceptionResponseDto = new ExceptionResponseDto(
            "Пользователь с таким никнеймом не найден.",
            e.getMessage(),
            LocalDateTime.now()
    );
    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(exceptionResponseDto);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponseDto> handleAccessDenied(AccessDeniedException e) {
        log.warn("Access denied: ", e);

        ExceptionResponseDto response = new ExceptionResponseDto(
                "Доступ запрещен. Вы не обладаете соответствующими правами.",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDto> handleGenericException(Exception e) {
        log.error("Unexpected error: ", e);
        ExceptionResponseDto response = new ExceptionResponseDto(
                "Внутренняя ошибка сервера",
                e.getMessage(),
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
