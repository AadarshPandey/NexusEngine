package com.nexusengine.core.domain.catalog.repository;

import com.nexusengine.core.domain.catalog.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    Page<Category> findByParentId(Long parentId, Pageable pageable);
    Page<Category> findByParentIdIsNull(Pageable pageable);
}
