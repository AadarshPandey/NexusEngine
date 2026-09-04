package com.nexusengine.core.repository;

import com.nexusengine.core.model.PmsProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

public interface PmsProductCategoryRepository extends JpaRepository<PmsProductCategory, Long>, JpaSpecificationExecutor<PmsProductCategory> {
    List<PmsProductCategory> findByParentIdOrderBySortDesc(Long parentId);
}
