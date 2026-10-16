package com.nexusengine.core.domain.catalog.repository;

import com.nexusengine.core.domain.catalog.entity.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    List<Product> findByNameContaining(String name);

    Page<Product> findByBrandIdAndDeletedAtIsNullAndIsPublished(Long brandId, Boolean isPublished, Pageable pageable);

    Page<Product> findByProductCategoryIdIn(List<Long> categoryIds, Pageable pageable);
}
