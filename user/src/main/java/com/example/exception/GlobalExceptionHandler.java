package com.example.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponseDto> handleAlreadyUserExists(Exception e) {
        log.warn("User already exists: ", e);
        ExceptionResponseDto response = new ExceptionResponseDto(
                "Пользователь с таким username уже существует.",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponseDto> handleEntityNotFound(EntityNotFoundException e) {
        log.info("Entity not found: ", e);
        ExceptionResponseDto response = new ExceptionResponseDto(
                "Ресурс не найден",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler({IllegalAccessException.class, IllegalStateException.class})
    public ResponseEntity<ExceptionResponseDto> handleBadRequest(Exception e) {
        log.warn("Invalid operation: ", e);
        ExceptionResponseDto response = new ExceptionResponseDto(
                "Операция невозможна в текущем состоянии системы",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponseDto> handleDataIntegrity(DataIntegrityViolationException e) {
        log.warn("Data integrity violation: ", e);
        ExceptionResponseDto response = new ExceptionResponseDto(
                "Ошибка целостности данных",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponseDto> handleAccessDenied(AccessDeniedException e) {
        log.warn("Access denied: ", e);
        ExceptionResponseDto response = new ExceptionResponseDto(
                "Доступ запрещен. Вы не обладаете соответствующими правами.",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDto> handleGenericException(Exception e) {
        log.error("Unexpected error: ", e);
        ExceptionResponseDto response = new ExceptionResponseDto(
                "Внутренняя ошибка сервера",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
