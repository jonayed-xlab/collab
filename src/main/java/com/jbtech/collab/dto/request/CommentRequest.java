package com.jbtech.collab.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    private Long mentionedUserId;
    private String content;
    private String entityType;
    private Long entityId;
}
