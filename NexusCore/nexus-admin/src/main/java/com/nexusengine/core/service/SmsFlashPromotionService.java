package com.nexusengine.core.service;

import com.nexusengine.core.model.SmsFlashPromotion;

import java.util.List;

/**
 * Represents the SmsFlashPromotionService component.
 * Provides core functionality and operations for SmsFlashPromotionService.
 */
public interface SmsFlashPromotionService {
        /**
     * Executes the operation.
     * @param flashPromotion the flashPromotion
     * @return the result of the operation
     */
    int create(SmsFlashPromotion flashPromotion);

        /**
     * Executes the operation.
     * @param id the id
     * @param flashPromotion the flashPromotion
     * @return the result of the operation
     */
    int update(Long id, SmsFlashPromotion flashPromotion);

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    int delete(Long id);

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
    SmsFlashPromotion getItem(Long id);

        /**
     * Executes the operation.
     * @param keyword the keyword
     * @param pageSize the pageSize
     * @param pageNum the pageNum
     * @return the result of the operation
     */
    List<SmsFlashPromotion> list(String keyword, Integer pageSize, Integer pageNum);
}
