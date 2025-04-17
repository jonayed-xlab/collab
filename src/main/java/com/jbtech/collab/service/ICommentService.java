package com.jbtech.collab.service;

import com.jbtech.collab.dto.request.CommentRequest;
import com.jbtech.collab.model.Comment;

import java.util.List;

public interface ICommentService {
    Comment createComment(CommentRequest request);
    List<Comment> getComments(String entityType, Long entityId);
    Comment updateComment(Long id, CommentRequest request);
}
