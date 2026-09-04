package com.nexusengine.core.repository;
import com.nexusengine.core.model.PmsProductAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
public interface PmsProductAttributeValueRepository extends JpaRepository<PmsProductAttributeValue, Long>, JpaSpecificationExecutor<PmsProductAttributeValue> {
    List<PmsProductAttributeValue> findByProductIdAndProductAttributeIdIn(Long productId, List<Long> attributeIds);
    void deleteByProductId(Long productId);
}
