package com.nexusengine.core.domain.promotion.repository;

import com.nexusengine.core.domain.promotion.entity.FlashSaleItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FlashSaleItemRepository extends JpaRepository<FlashSaleItem, Long> {
    Page<FlashSaleItem> findByFlashPromotionIdAndFlashPromotionSessionId(Long flashPromotionId, Long flashPromotionSessionId, Pageable pageable);
    
    List<FlashSaleItem> findByFlashPromotionIdAndFlashPromotionSessionId(Long flashPromotionId, Long flashPromotionSessionId);
    
    Optional<FlashSaleItem> findByFlashPromotionIdAndFlashPromotionSessionIdAndProductId(Long flashPromotionId, Long flashPromotionSessionId, Long productId);
}
