package com.nexusengine.core.service;

import com.nexusengine.core.model.SmsHomeBrand;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Represents the SmsHomeBrandService component.
 * Provides core functionality and operations for SmsHomeBrandService.
 */
public interface SmsHomeBrandService {
        /**
     * Executes the operation.
     * @param homeBrandList the homeBrandList
     * @return the result of the operation
     */
    @Transactional
    int create(List<SmsHomeBrand> homeBrandList);

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
     * @param brandName the brandName
     * @param recommendStatus the recommendStatus
     * @param pageSize the pageSize
     * @param pageNum the pageNum
     * @return the result of the operation
     */
    List<SmsHomeBrand> list(String brandName, Integer recommendStatus, Integer pageSize, Integer pageNum);
}
