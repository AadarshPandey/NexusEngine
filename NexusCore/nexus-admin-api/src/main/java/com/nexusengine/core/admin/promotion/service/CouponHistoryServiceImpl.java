package com.nexusengine.core.admin.promotion.service;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.domain.promotion.repository.MemberCouponRepository;

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
import com.nexusengine.core.domain.promotion.entity.MemberCoupon;
import org.springframework.beans.factory.annotation.Autowired;
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
