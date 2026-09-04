package com.nexusengine.core.repository;

import com.nexusengine.core.model.PmsProductCategoryAttributeRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

public interface PmsProductCategoryAttributeRelationRepository extends JpaRepository<PmsProductCategoryAttributeRelation, Long>, JpaSpecificationExecutor<PmsProductCategoryAttributeRelation> {
    void deleteByProductCategoryId(Long productCategoryId);
}
