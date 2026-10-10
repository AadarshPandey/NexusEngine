package com.nexusengine.core.domain.catalog.repository;
import com.nexusengine.core.domain.catalog.entity.ProductAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
public interface ProductAttributeValueRepository extends JpaRepository<ProductAttributeValue, Long>, JpaSpecificationExecutor<ProductAttributeValue> {
    List<ProductAttributeValue> findByProductIdAndProductAttributeIdIn(Long productId, List<Long> attributeIds);
    List<ProductAttributeValue> findByProductId(Long productId);
    void deleteByProductId(Long productId);
}
