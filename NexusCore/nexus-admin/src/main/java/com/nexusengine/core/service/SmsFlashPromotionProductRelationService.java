package com.nexusengine.core.service;

import com.nexusengine.core.dto.SmsFlashPromotionProduct;
import com.nexusengine.core.model.SmsFlashPromotionProductRelation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Represents the SmsFlashPromotionProductRelationService component.
 * Provides core functionality and operations for SmsFlashPromotionProductRelationService.
 */
public interface SmsFlashPromotionProductRelationService {
        /**
     * Executes the operation.
     * @param relationList the relationList
     * @return the result of the operation
     */
    @Transactional
    int create(List<SmsFlashPromotionProductRelation> relationList);

        /**
     * Executes the operation.
     * @param id the id
     * @param relation the relation
     * @return the result of the operation
     */
    int update(Long id, SmsFlashPromotionProductRelation relation);

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
    SmsFlashPromotionProductRelation getItem(Long id);

    /**
     *
     */
    List<SmsFlashPromotionProduct> list(Long flashPromotionId, Long flashPromotionSessionId, Integer pageSize, Integer pageNum);

        /**
     * Executes the operation.
     * @param flashPromotionId the flashPromotionId
     * @param flashPromotionSessionId the flashPromotionSessionId
     * @return the result of the operation
     */
    long getCount(Long flashPromotionId,Long flashPromotionSessionId);
}
