package com.example.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentResponse (
        Long id,
        String content,
        String authorName,
        LocalDateTime createdAt
){ }
