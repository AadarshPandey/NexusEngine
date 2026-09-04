package com.nexusengine.core.service;

import com.nexusengine.core.model.SmsCouponHistory;

import java.util.List;

/**
 * Auto-generated documentation
 * Created by macro on 2018/11/6.
 */
public interface SmsCouponHistoryService {
    /**
     * Auto-generated documentation
     * Auto-generated documentation
     * Auto-generated documentation
     * Auto-generated documentation
     */
    List<SmsCouponHistory> list(Long couponId, Integer useStatus, String orderSn, Integer pageSize, Integer pageNum);
}
