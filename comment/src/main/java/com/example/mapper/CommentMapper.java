package com.example.mapper;

import com.example.dto.request.CommentRequest;
import com.example.dto.response.CommentResponse;
import com.example.entity.Advertisement;
import com.example.entity.Comment;
import com.example.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    public Comment toEntity(CommentRequest request, Advertisement advertisement, User user) {
        Comment comment = new Comment();
        comment.setAdvertisement(advertisement);
        comment.setUser(user);
        comment.setContent(request.content());
        return comment;
    }

    public CommentResponse toResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .authorName(comment.getUser().getUsername())
                .createdAt(comment.getCreated())
                .build();
    }

    public List<CommentResponse> toListResponse(List<Comment> comments) {
        return comments.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
