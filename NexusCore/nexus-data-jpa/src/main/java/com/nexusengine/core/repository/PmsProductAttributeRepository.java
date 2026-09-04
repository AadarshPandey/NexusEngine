package com.nexusengine.core.repository;

import com.nexusengine.core.model.PmsProductAttribute;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PmsProductAttributeRepository extends JpaRepository<PmsProductAttribute, Long> {
    List<PmsProductAttribute> findByProductAttributeCategoryIdAndType(Long productAttributeCategoryId, Integer type, Pageable pageable);
    
    List<PmsProductAttribute> findByProductAttributeCategoryId(Long productAttributeCategoryId);
}
