package com.jbtech.collab.model;

import com.jbtech.collab.utils.WorkPackageEnum;
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
public class WorkPackageDynamicMapping extends BaseModel {
    private Long parentWorkPackageId;
    @Enumerated(EnumType.STRING)
    private WorkPackageEnum parentWorkPackageType;
    private Long childWorkPackageId;
    @Enumerated(EnumType.STRING)
    private WorkPackageEnum childWorkPackageType;
}
