package com.nexusengine.core.service;

import com.nexusengine.core.dto.SmsFlashPromotionProduct;
import com.nexusengine.core.model.SmsFlashPromotionProductRelation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/11/16.
 */
public interface SmsFlashPromotionProductRelationService {
    /**
     * Auto-generated documentation
     */
    @Transactional
    int create(List<SmsFlashPromotionProductRelation> relationList);

    /**
     * Auto-generated documentation
     */
    int update(Long id, SmsFlashPromotionProductRelation relation);

    /**
     * Auto-generated documentation
     */
    int delete(Long id);

    /**
     * Auto-generated documentation
     */
    SmsFlashPromotionProductRelation getItem(Long id);

    /**
     * Auto-generated documentation
     *
     * Auto-generated documentation
     * Auto-generated documentation
     */
    List<SmsFlashPromotionProduct> list(Long flashPromotionId, Long flashPromotionSessionId, Integer pageSize, Integer pageNum);

    /**
     * Auto-generated documentation
     * Auto-generated documentation
     * Auto-generated documentation
     */
    long getCount(Long flashPromotionId,Long flashPromotionSessionId);
}
