package com.nexusengine.core.domain.catalog.repository;

import com.nexusengine.core.domain.catalog.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    List<Product> findByNameContaining(String name);
    Page<Product> findByBrandIdAndDeleteStatusAndPublishStatus(Long brandId, Integer deleteStatus, Integer publishStatus, Pageable pageable);
}
