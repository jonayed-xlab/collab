package com.jbtech.collab.resource;

import com.jbtech.collab.dto.request.CommentRequest;
import com.jbtech.collab.dto.response.ApiResponse;
import com.jbtech.collab.model.Comment;
import com.jbtech.collab.service.ICommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${app.base.url}/comments")
@RequiredArgsConstructor
public class CommentResource extends BaseResource {

    private final ICommentService commentService;

    @PostMapping
    public ApiResponse<Comment> createComment(@RequestBody CommentRequest request) {
        return ApiResponse.success(
                commentService.createComment(request)
        );
    }

    @GetMapping
    public ApiResponse<List<Comment>> getComments(
            @RequestParam String entityType,
            @RequestParam Long entityId) {
        return ApiResponse.success(
                commentService.getComments(entityType, entityId)
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<Comment> updateComment(@PathVariable Long id, @RequestBody CommentRequest request) {
        return ApiResponse.success(
                commentService.updateComment(id, request)
        );
    }
}
