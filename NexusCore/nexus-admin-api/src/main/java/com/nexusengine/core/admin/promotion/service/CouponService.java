package com.nexusengine.core.admin.promotion.service;

import com.nexusengine.core.domain.promotion.entity.Coupon;

import com.nexusengine.core.admin.promotion.dto.CouponParam;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Represents the CouponService component.
 * Provides core functionality and operations for CouponService.
 */
public interface CouponService {
        /**
     * Executes the operation.
     * @param couponParam the couponParam
     * @return the result of the operation
     */
    @Transactional
    int create(CouponParam couponParam);

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
    int update(Long id, CouponParam couponParam);

        /**
     * Executes the operation.
     * @param name the name
     * @param type the type
     * @param pageSize the pageSize
     * @param pageNum the pageNum
     * @return the result of the operation
     */
    org.springframework.data.domain.Page<Coupon> list(String name, Integer type, Integer pageSize, Integer pageNum);

        /**
     * Executes the operation.
     * @param id the id
     * @return the result of the operation
     */
    CouponParam getItem(Long id);
}
