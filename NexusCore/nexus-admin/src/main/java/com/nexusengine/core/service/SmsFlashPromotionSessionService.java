package com.nexusengine.core.service;

import com.nexusengine.core.dto.SmsFlashPromotionSessionDetail;
import com.nexusengine.core.model.SmsFlashPromotionSession;

import java.util.List;

/**
 * Represents the SmsFlashPromotionSessionService component.
 * Provides core functionality and operations for SmsFlashPromotionSessionService.
 */
public interface SmsFlashPromotionSessionService {
        /**
     * Executes the operation.
     * @param promotionSession the promotionSession
     * @return the result of the operation
     */
    int create(SmsFlashPromotionSession promotionSession);

        /**
     * Executes the operation.
     * @param id the id
     * @param promotionSession the promotionSession
     * @return the result of the operation
     */
    int update(Long id, SmsFlashPromotionSession promotionSession);

        /**
     * Executes the operation.
     * @param id the id
     * @param status the status
     * @return the result of the operation
     */
    int updateStatus(Long id, Integer status);

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    int delete(Long id);

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    SmsFlashPromotionSession getItem(Long id);

        /**
     * Executes the operation.
     * @return the result of the operation
     */
    List<SmsFlashPromotionSession> list();

        /**
     * Executes the operation.
     * @param flashPromotionId the flashPromotionId
     * @return the result of the operation
     */
    List<SmsFlashPromotionSessionDetail> selectList(Long flashPromotionId);
}
