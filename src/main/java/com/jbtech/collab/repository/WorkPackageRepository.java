package com.jbtech.collab.repository;

import com.jbtech.collab.dto.response.WorkPackageResponseWrapper;
import com.jbtech.collab.model.WorkPackage;
import com.jbtech.collab.utils.WorkPackageEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkPackageRepository extends JpaRepository<WorkPackage, Long> {
    List<WorkPackage> findByProjectId(Long projectId);
    List<WorkPackage> findByAssignedTo(Long userId);
    List<WorkPackage> findByProjectIdAndWorkPackageType(Long projectId, WorkPackageEnum workPackageEnum);
    List<WorkPackage> findByTitleContainingIgnoreCaseAndWorkPackageType(String title, WorkPackageEnum workPackageType);
    List<WorkPackage> findByTitleContainingIgnoreCase(String title);
    List<WorkPackage> findByWorkPackageType(WorkPackageEnum workPackageType);
}
