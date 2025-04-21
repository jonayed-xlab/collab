package com.jbtech.collab.service.impl;

import com.jbtech.collab.dto.request.CommentRequest;
import com.jbtech.collab.exception.ApiException;
import com.jbtech.collab.model.Comment;
import com.jbtech.collab.repository.CommentRepository;
import com.jbtech.collab.service.BaseService;
import com.jbtech.collab.service.ICommentService;
import com.jbtech.collab.utils.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService extends BaseService implements ICommentService {

    private final CommentRepository commentRepo;

    public CommentService(JwtUtil jwtUtil, CommentRepository commentRepo) {
        super(jwtUtil);
        this.commentRepo = commentRepo;
    }

    @Override
    @Transactional
    public Comment createComment(CommentRequest request) {

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setEntityId(request.getEntityId());
        comment.setEntityType(request.getEntityType());
        comment.setCreatedBy(getCurrentUser().getName());
        comment.setCreatedAt(LocalDateTime.now());

        return commentRepo.save(comment);
    }

    @Override
    public List<Comment> getComments(String entityType, Long entityId) {
        return commentRepo.findByEntityTypeAndEntityId(entityType, entityId);
    }

    @Override
    public Comment updateComment(Long id, CommentRequest request) {

        Comment comment = commentRepo.findById(id)
                .orElseThrow(() -> new ApiException("E404", "Comment not found"));

        comment.setContent(request.getContent());
        comment.setUpdatedBy(getCurrentUser().getName());
        comment.setUpdatedAt(LocalDateTime.now());

        return commentRepo.save(comment);
    }
}
