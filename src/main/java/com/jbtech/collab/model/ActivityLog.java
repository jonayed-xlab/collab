package com.jbtech.collab.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ActivityLog extends BaseModel {

    private String entityType;
    private Long entityId;

    private String fieldName;
    private String oldValue;
    private String newValue;
}
