package com.jbtech.collab.model;

import com.jbtech.collab.utils.ProjectStatusEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Project extends BaseModel {
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private ProjectStatusEnum status;
}
