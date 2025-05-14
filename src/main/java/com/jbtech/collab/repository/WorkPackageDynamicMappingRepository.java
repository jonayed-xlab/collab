package com.jbtech.collab.repository;

import com.jbtech.collab.model.WorkPackageDynamicMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkPackageDynamicMappingRepository extends JpaRepository<WorkPackageDynamicMapping, Long> {
    List<WorkPackageDynamicMapping> findByParentWorkPackageId(Long id);

    List<WorkPackageDynamicMapping> findByChildWorkPackageId(Long id);
}
