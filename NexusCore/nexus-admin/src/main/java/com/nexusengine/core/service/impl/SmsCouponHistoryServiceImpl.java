package com.nexusengine.core.service.impl;

import com.nexusengine.core.repository.SmsCouponHistoryRepository;
import com.nexusengine.core.model.SmsCouponHistory;
import com.nexusengine.core.service.SmsCouponHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SmsCouponHistoryServiceImpl implements SmsCouponHistoryService {
    @Autowired
    private SmsCouponHistoryRepository historyRepository;

    @Override
    public List<SmsCouponHistory> list(Long couponId, Integer useStatus, String orderSn, Integer pageSize, Integer pageNum) {
        if (couponId != null && useStatus != null) {
            return historyRepository.findByCouponIdAndUseStatus(couponId, useStatus);
        }
        if (couponId != null) {
            return historyRepository.findByCouponId(couponId);
        }
        return historyRepository.findAll();
    }
}
