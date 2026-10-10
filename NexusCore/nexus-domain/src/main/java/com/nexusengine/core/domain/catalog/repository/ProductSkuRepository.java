package com.nexusengine.core.domain.catalog.repository;
import com.nexusengine.core.domain.catalog.entity.ProductSku;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ProductSkuRepository extends JpaRepository<ProductSku, Long> {
    List<ProductSku> findByProductId(Long productId);
    List<ProductSku> findByProductIdAndSkuCodeContaining(Long productId, String skuCode);
    void deleteByProductId(Long productId);
}
