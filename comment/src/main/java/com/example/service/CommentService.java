package com.example.service;

import com.example.dto.request.CommentRequest;
import com.example.dto.response.CommentResponse;

import java.util.List;

public interface CommentService {

    CommentResponse create(Long advertisementId, CommentRequest request, String username);

    List<CommentResponse> getCommentsByAdvertisement(Long advertisementId);

    void deleteComment(Long commentId, String username);
}
