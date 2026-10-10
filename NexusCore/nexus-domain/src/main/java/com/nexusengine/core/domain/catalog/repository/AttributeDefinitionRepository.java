package com.nexusengine.core.domain.catalog.repository;

import com.nexusengine.core.domain.catalog.entity.AttributeDefinition;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttributeDefinitionRepository extends JpaRepository<AttributeDefinition, Long> {
    Page<AttributeDefinition> findByProductAttributeCategoryIdAndType(Long productAttributeCategoryId, Integer type, Pageable pageable);
    
    List<AttributeDefinition> findByProductAttributeCategoryId(Long productAttributeCategoryId);
}
