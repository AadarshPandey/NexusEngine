package com.nexusengine.core.service;

import com.nexusengine.core.model.SmsFlashPromotion;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/11/16.
 */
public interface SmsFlashPromotionService {
    /**
     * Auto-generated documentation
     */
    int create(SmsFlashPromotion flashPromotion);

    /**
     * Auto-generated documentation
     */
    int update(Long id, SmsFlashPromotion flashPromotion);

    /**
     * Auto-generated documentation
     */
    int delete(Long id);

    /**
     * Auto-generated documentation
     */
    int updateStatus(Long id, Integer status);

    /**
     * Auto-generated documentation
     */
    SmsFlashPromotion getItem(Long id);

    /**
     * Auto-generated documentation
     */
    List<SmsFlashPromotion> list(String keyword, Integer pageSize, Integer pageNum);
}
