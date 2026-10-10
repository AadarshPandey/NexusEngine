package com.nexusengine.core.domain.catalog.repository;

import com.nexusengine.core.domain.catalog.entity.ProductPriceChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductPriceChangeRepository extends JpaRepository<ProductPriceChange, Long>, JpaSpecificationExecutor<ProductPriceChange> {
    java.util.List<ProductPriceChange> findByProductId(Long productId);
}
