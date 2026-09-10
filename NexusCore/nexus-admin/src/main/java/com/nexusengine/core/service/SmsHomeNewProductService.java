package com.nexusengine.core.service;

import com.nexusengine.core.model.SmsHomeNewProduct;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Represents the SmsHomeNewProductService component.
 * Provides core functionality and operations for SmsHomeNewProductService.
 */
public interface SmsHomeNewProductService {
        /**
     * Executes the operation.
     * @param homeNewProductList the homeNewProductList
     * @return the result of the operation
     */
    @Transactional
    int create(List<SmsHomeNewProduct> homeNewProductList);

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
    List<SmsHomeNewProduct> list(String productName, Integer recommendStatus, Integer pageSize, Integer pageNum);
}
