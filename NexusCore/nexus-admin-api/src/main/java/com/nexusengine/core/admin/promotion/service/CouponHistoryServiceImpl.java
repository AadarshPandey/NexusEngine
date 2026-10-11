package com.nexusengine.core.admin.promotion.service;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.domain.promotion.repository.MemberCouponRepository;


import com.nexusengine.core.domain.promotion.entity.MemberCoupon;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CouponHistoryServiceImpl implements CouponHistoryService {
    private final MemberCouponRepository historyRepository;

    @Override
    public List<MemberCoupon> list(Long couponId, Integer useStatus, String orderSn, Integer pageSize, Integer pageNum) {
        if (couponId != null && useStatus != null) {
            return historyRepository.findByCouponIdAndUseStatus(couponId, useStatus);
        }
        if (couponId != null) {
            return historyRepository.findByCouponId(couponId);
        }
        return historyRepository.findAll(PageRequest.of(0, 1000)).getContent();
    }
}
