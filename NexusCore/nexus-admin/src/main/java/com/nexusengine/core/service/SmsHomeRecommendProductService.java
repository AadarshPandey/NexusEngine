package com.nexusengine.core.service;

import com.nexusengine.core.model.SmsHomeRecommendProduct;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Represents the SmsHomeRecommendProductService component.
 * Provides core functionality and operations for SmsHomeRecommendProductService.
 */
public interface SmsHomeRecommendProductService {
        /**
     * Executes the operation.
     * @param homeRecommendProductList the homeRecommendProductList
     * @return the result of the operation
     */
    @Transactional
    int create(List<SmsHomeRecommendProduct> homeRecommendProductList);

        /**
     * Executes the operation.
     * @param id the id
     * @param sort the sort
     * @return the result of the operation
     */
    int updateSort(Long id, Integer sort);

        /**
     * Executes the operation.
     * @param ids the ids
     * @return the result of the operation
     */
    int delete(List<Long> ids);

        /**
     * Executes the operation.
     * @param ids the ids
     * @param recommendStatus the recommendStatus
     * @return the result of the operation
     */
    int updateRecommendStatus(List<Long> ids, Integer recommendStatus);

        /**
     * Executes the operation.
     * @param productName the productName
     * @param recommendStatus the recommendStatus
     * @param pageSize the pageSize
     * @param pageNum the pageNum
     * @return the result of the operation
     */
    List<SmsHomeRecommendProduct> list(String productName, Integer recommendStatus, Integer pageSize, Integer pageNum);
}
