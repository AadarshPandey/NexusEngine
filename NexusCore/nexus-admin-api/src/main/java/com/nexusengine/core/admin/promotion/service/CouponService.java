package com.nexusengine.core.admin.promotion.service;

import com.nexusengine.core.domain.promotion.entity.Coupon;

import com.nexusengine.core.domain.catalog.entity.*;
import com.nexusengine.core.domain.ordering.entity.*;
import com.nexusengine.core.domain.customer.entity.*;
import com.nexusengine.core.domain.promotion.entity.*;
import com.nexusengine.core.domain.iam.entity.*;
import com.nexusengine.core.domain.catalog.repository.*;
import com.nexusengine.core.domain.ordering.repository.*;
import com.nexusengine.core.domain.customer.repository.*;
import com.nexusengine.core.domain.promotion.repository.*;
import com.nexusengine.core.domain.iam.repository.*;
import com.nexusengine.core.admin.catalog.dto.*;
import com.nexusengine.core.admin.catalog.service.*;
import com.nexusengine.core.admin.analytics.service.*;
import com.nexusengine.core.admin.ordering.dto.*;
import com.nexusengine.core.admin.ordering.service.*;
import com.nexusengine.core.admin.promotion.dto.*;
import com.nexusengine.core.admin.promotion.service.*;
import com.nexusengine.core.admin.ai.service.*;
import com.nexusengine.core.admin.infra.dto.*;
import com.nexusengine.core.admin.infra.service.*;
import com.nexusengine.core.admin.iam.dto.*;
import com.nexusengine.core.admin.iam.service.*;
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
