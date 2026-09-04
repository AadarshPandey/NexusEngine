package com.nexusengine.core.service;

import com.nexusengine.core.dto.SmsFlashPromotionSessionDetail;
import com.nexusengine.core.model.SmsFlashPromotionSession;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/11/16.
 */
public interface SmsFlashPromotionSessionService {
    /**
     * Auto-generated documentation
     */
    int create(SmsFlashPromotionSession promotionSession);

    /**
     * Auto-generated documentation
     */
    int update(Long id, SmsFlashPromotionSession promotionSession);

    /**
     * Auto-generated documentation
     */
    int updateStatus(Long id, Integer status);

    /**
     * Auto-generated documentation
     */
    int delete(Long id);

    /**
     * Auto-generated documentation
     */
    SmsFlashPromotionSession getItem(Long id);

    /**
     * Auto-generated documentation
     */
    List<SmsFlashPromotionSession> list();

    /**
     * Auto-generated documentation
     */
    List<SmsFlashPromotionSessionDetail> selectList(Long flashPromotionId);
}
