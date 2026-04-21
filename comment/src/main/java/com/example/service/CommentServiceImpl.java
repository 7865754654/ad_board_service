package com.example.service;

import com.example.entity.Advertisement;
import com.example.entity.Comment;
import com.example.entity.User;
import com.example.mapper.CommentMapper;
import com.example.repository.AdvertisementRepository;
import com.example.repository.UserRepository;
import com.example.dto.request.CommentRequest;
import com.example.dto.response.CommentResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.repository.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final AdvertisementRepository advertisementRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Transactional
    public CommentResponse create(Long advertisementId, CommentRequest request, String username) {

        Advertisement advertisement = advertisementRepository.findById(advertisementId)
                .orElseThrow(() -> new EntityNotFoundException("Advertisement not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Comment comment = commentMapper.toEntity(request, advertisement, user);

        Comment saved = commentRepository.save(comment);

        return commentMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByAdvertisement(Long advertisementId) {
       List<Comment> comments = commentRepository.findByAdvertisementIdOrderByCreatedDesc(advertisementId);

       return commentMapper.toListResponse(comments);
    }

    @Transactional
    public void deleteComment(Long commentId, String username) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        if (!comment.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("You can only delete your own comments");
        }

        commentRepository.delete(comment);
    }
}
