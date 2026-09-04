package com.nexusengine.core.service;

import com.nexusengine.core.dto.SmsCouponParam;
import com.nexusengine.core.model.SmsCoupon;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/8/28.
 */
public interface SmsCouponService {
    /**
     * Auto-generated documentation
     */
    @Transactional
    int create(SmsCouponParam couponParam);

    /**
     * Auto-generated documentation
     */
    @Transactional
    int delete(Long id);

    /**
     * Auto-generated documentation
     */
    @Transactional
    int update(Long id, SmsCouponParam couponParam);

    /**
     * Auto-generated documentation
     */
    List<SmsCoupon> list(String name, Integer type, Integer pageSize, Integer pageNum);

    /**
     * Auto-generated documentation
     * Auto-generated documentation
     */
    SmsCouponParam getItem(Long id);
}
