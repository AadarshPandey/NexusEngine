package com.nexusengine.core.domain.promotion.repository;
import com.nexusengine.core.domain.promotion.entity.QuantityTierDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface QuantityTierDiscountRepository extends JpaRepository<QuantityTierDiscount, Long> {
    List<QuantityTierDiscount> findByProductId(Long productId);
    void deleteByProductId(Long productId);
}
