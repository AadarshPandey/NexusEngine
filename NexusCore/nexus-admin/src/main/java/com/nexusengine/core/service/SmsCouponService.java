package com.nexusengine.core.service;

import com.nexusengine.core.dto.SmsCouponParam;
import com.nexusengine.core.model.SmsCoupon;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Represents the SmsCouponService component.
 * Provides core functionality and operations for SmsCouponService.
 */
public interface SmsCouponService {
        /**
     * Executes the operation.
     * @param couponParam the couponParam
     * @return the result of the operation
     */
    @Transactional
    int create(SmsCouponParam couponParam);

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    @Transactional
    int delete(Long id);

        /**
     * Executes the operation.
     * @param id the id
     * @param couponParam the couponParam
     * @return the result of the operation
     */
    @Transactional
    int update(Long id, SmsCouponParam couponParam);

        /**
     * Executes the operation.
     * @param name the name
     * @param type the type
     * @param pageSize the pageSize
     * @param pageNum the pageNum
     * @return the result of the operation
     */
    List<SmsCoupon> list(String name, Integer type, Integer pageSize, Integer pageNum);

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    SmsCouponParam getItem(Long id);
}
