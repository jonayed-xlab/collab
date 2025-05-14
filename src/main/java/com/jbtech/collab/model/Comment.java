package com.jbtech.collab.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseModel {
    private Long mentionedUserId;
    private Long entityId;
    private String entityType;
    private String content;
}
