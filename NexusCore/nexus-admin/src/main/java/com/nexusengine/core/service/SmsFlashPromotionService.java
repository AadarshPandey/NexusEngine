package com.nexusengine.core.service;

import com.nexusengine.core.model.SmsFlashPromotion;
import org.springframework.data.domain.Page;

public interface SmsFlashPromotionService {
    int create(SmsFlashPromotion flashPromotion);
    int update(Long id, SmsFlashPromotion flashPromotion);
    int delete(Long id);
    int updateStatus(Long id, Integer status);
    SmsFlashPromotion getItem(Long id);
    Page<SmsFlashPromotion> list(String keyword, Integer pageNum, Integer pageSize);
}
