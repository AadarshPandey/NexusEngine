package com.nexusengine.core.repository;

import com.nexusengine.core.model.SmsFlashPromotionProductRelation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SmsFlashPromotionProductRelationRepository extends JpaRepository<SmsFlashPromotionProductRelation, Long> {
    Page<SmsFlashPromotionProductRelation> findByFlashPromotionIdAndFlashPromotionSessionId(Long flashPromotionId, Long flashPromotionSessionId, Pageable pageable);
    
    List<SmsFlashPromotionProductRelation> findByFlashPromotionIdAndFlashPromotionSessionId(Long flashPromotionId, Long flashPromotionSessionId);
    
    Optional<SmsFlashPromotionProductRelation> findByFlashPromotionIdAndFlashPromotionSessionIdAndProductId(Long flashPromotionId, Long flashPromotionSessionId, Long productId);
}
