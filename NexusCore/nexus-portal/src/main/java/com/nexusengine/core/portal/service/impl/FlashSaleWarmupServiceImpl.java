package com.nexusengine.core.portal.service.impl;

import com.nexusengine.core.model.SmsFlashPromotionProductRelation;
import com.nexusengine.core.repository.SmsFlashPromotionProductRelationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlashSaleWarmupServiceImpl {

    private final SmsFlashPromotionProductRelationRepository relationRepository;
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * In a real application, this would be triggered by a distributed scheduled job (e.g. XXL-JOB)
     * a few minutes before each session starts.
     */
    // @PostConstruct
    public void warmupFlashSaleStock(Long flashPromotionId, Long flashPromotionSessionId) {
        log.info("Starting Redis warmup for Flash Promotion {}, Session {}", flashPromotionId, flashPromotionSessionId);
        
        List<SmsFlashPromotionProductRelation> relations = 
            relationRepository.findByFlashPromotionIdAndFlashPromotionSessionId(flashPromotionId, flashPromotionSessionId);
            
        for (SmsFlashPromotionProductRelation relation : relations) {
            String stockKey = "flash:stock:" + flashPromotionId + ":" + flashPromotionSessionId + ":" + relation.getProductId();
            stringRedisTemplate.opsForValue().set(stockKey, String.valueOf(relation.getFlashPromotionCount()));
            log.info("Warmed up {} = {}", stockKey, relation.getFlashPromotionCount());
        }
        
        log.info("Redis warmup complete.");
    }
}
