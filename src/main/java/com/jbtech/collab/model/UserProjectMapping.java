package com.jbtech.collab.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserProjectMapping extends BaseModel{
    private Long userId;
    private Long projectId;
}
