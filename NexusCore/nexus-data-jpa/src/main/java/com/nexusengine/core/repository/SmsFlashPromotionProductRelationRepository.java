package com.nexusengine.core.repository;
import com.nexusengine.core.model.SmsFlashPromotionProductRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface SmsFlashPromotionProductRelationRepository extends JpaRepository<SmsFlashPromotionProductRelation, Long> {
    List<SmsFlashPromotionProductRelation> findByFlashPromotionIdAndFlashPromotionSessionId(Long flashPromotionId, Long flashPromotionSessionId);
    long countByFlashPromotionIdAndFlashPromotionSessionId(Long flashPromotionId, Long flashPromotionSessionId);
}
