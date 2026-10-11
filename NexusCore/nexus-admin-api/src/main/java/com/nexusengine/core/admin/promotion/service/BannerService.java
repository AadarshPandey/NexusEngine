package com.nexusengine.core.admin.promotion.service;

import com.nexusengine.core.domain.promotion.entity.Banner;



import java.util.List;


/**
 * Represents the BannerService component.
 * Provides core functionality and operations for BannerService.
 */
public interface BannerService {
        /**
     * Executes the operation.
     * @param advertise the advertise
     * @return the result of the operation
     */
    int create(Banner advertise);

        /**
     * Executes the operation.
     * @param ids the ids
     * @return the result of the operation
     */
    int delete(List<Long> ids);

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
    Banner getItem(Long id);

        /**
     * Executes the operation.
     * @param id the id
     * @param advertise the advertise
     * @return the result of the operation
     */
    int update(Long id, Banner advertise);

        /**
     * Executes the operation.
     * @param name the name
     * @param type the type
     * @param endTime the endTime
     * @param pageSize the pageSize
     * @param pageNum the pageNum
     * @return the result of the operation
     */
    List<Banner> list(String name, Integer type, String endTime, Integer pageSize, Integer pageNum);
}
