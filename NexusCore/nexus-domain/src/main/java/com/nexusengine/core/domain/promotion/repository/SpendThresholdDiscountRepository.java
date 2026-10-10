package com.nexusengine.core.domain.promotion.repository;
import com.nexusengine.core.domain.promotion.entity.SpendThresholdDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface SpendThresholdDiscountRepository extends JpaRepository<SpendThresholdDiscount, Long> {
    List<SpendThresholdDiscount> findByProductId(Long productId);
    void deleteByProductId(Long productId);
}
