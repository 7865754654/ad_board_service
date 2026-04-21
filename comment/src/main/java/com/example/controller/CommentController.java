package com.example.controller;

import com.example.dto.request.CommentRequest;
import com.example.dto.response.CommentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.example.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/advertisements/{adId}/comments")
@RequiredArgsConstructor
@Tag(name = "Сервис работы с комментариями", description = "API для управления комментариями к объявлениям")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Создание нового комментария")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Комментарий успешно создан"),
            @ApiResponse(responseCode = "400", description = "Неверно переданы данные"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован"),
            @ApiResponse(responseCode = "404", description = "Объявление или пользователь не найдены"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<CommentResponse> createComment(
            @Parameter(description = "ID объявления", required = true)
            @PathVariable("adId") Long adId,

            @Parameter(description = "Данные для создания комментария", required = true)
            @RequestBody @Valid CommentRequest request,

            @Parameter(description = "Данные аутентифицированного пользователя", hidden = true)
            @AuthenticationPrincipal UserDetails userDetails) {

        CommentResponse comment = commentService.create(
                adId,
                request,
                userDetails.getUsername()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @GetMapping
    @Operation(summary = "Получение всех комментариев объявления")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список комментариев успешно получен"),
            @ApiResponse(responseCode = "404", description = "Объявление не найдено"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<CommentResponse>> getComments(
            @Parameter(description = "ID объявления", required = true)
            @PathVariable("adId") Long adId) {

        List<CommentResponse> comments = commentService.getCommentsByAdvertisement(adId);

        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Удаление комментария по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Комментарий успешно удалён"),
            @ApiResponse(responseCode = "401", description = "Пользователь не аутентифицирован"),
            @ApiResponse(responseCode = "403", description = "Нет прав на удаление"),
            @ApiResponse(responseCode = "404", description = "Комментарий или объявление не найдены"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(
            @Parameter(description = "ID объявления", required = true)
            @PathVariable("adId") Long adId,

            @Parameter(description = "ID комментария", required = true)
            @PathVariable("commentId") Long commentId,

            @Parameter(description = "Данные аутентифицированного пользователя", hidden = true)
            @AuthenticationPrincipal UserDetails userDetails) {

        commentService.deleteComment(commentId, userDetails.getUsername());
    }
}
