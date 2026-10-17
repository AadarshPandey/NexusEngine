package com.nexusengine.core.domain.catalog.repository;

import com.nexusengine.core.domain.catalog.entity.ProductMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductMediaRepository extends JpaRepository<ProductMedia, Long> {
    List<ProductMedia> findByProductIdOrderBySortOrderAsc(Long productId);
    void deleteByProductId(Long productId);
}
