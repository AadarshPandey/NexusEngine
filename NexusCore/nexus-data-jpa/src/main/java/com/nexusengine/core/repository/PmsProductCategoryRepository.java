package com.nexusengine.core.repository;

import com.nexusengine.core.model.PmsProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

public interface PmsProductCategoryRepository extends JpaRepository<PmsProductCategory, Long>, JpaSpecificationExecutor<PmsProductCategory> {
    Page<PmsProductCategory> findByParentIdOrderBySortDesc(Long parentId, Pageable pageable);
    Page<PmsProductCategory> findByParentIdIsNullOrderBySortDesc(Pageable pageable);
}
