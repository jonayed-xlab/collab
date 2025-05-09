package com.jbtech.collab.dto.response;

import com.jbtech.collab.model.WorkPackage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkPackageResponseWrapper {
    private WorkPackage workPackage;
    private List<WorkPackage> relatedWorkPackages;
    private List<WorkPackage> childWorkPackages;
}
