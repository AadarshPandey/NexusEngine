package com.nexusengine.core.domain.catalog.repository;

import com.nexusengine.core.domain.catalog.entity.CategoryAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

public interface CategoryAttributeRepository extends JpaRepository<CategoryAttribute, Long>, JpaSpecificationExecutor<CategoryAttribute> {
    void deleteByProductCategoryId(Long productCategoryId);
}
