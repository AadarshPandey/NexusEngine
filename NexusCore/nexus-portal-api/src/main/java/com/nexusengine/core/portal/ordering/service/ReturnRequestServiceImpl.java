package com.nexusengine.core.portal.ordering.service;

import lombok.RequiredArgsConstructor;
import com.nexusengine.core.portal.catalog.dto.*;
import com.nexusengine.core.portal.catalog.service.*;
import com.nexusengine.core.portal.catalog.dao.*;
import com.nexusengine.core.portal.payment.service.*;
import com.nexusengine.core.portal.ordering.dto.*;
import com.nexusengine.core.portal.ordering.service.*;
import com.nexusengine.core.portal.ordering.dao.*;
import com.nexusengine.core.portal.customer.dto.*;
import com.nexusengine.core.portal.customer.service.*;
import com.nexusengine.core.portal.promotion.dto.*;
import com.nexusengine.core.portal.promotion.service.*;
import com.nexusengine.core.portal.promotion.strategy.*;
import com.nexusengine.core.portal.ai.service.*;
import com.nexusengine.core.portal.home.dto.*;
import com.nexusengine.core.portal.home.service.*;
import com.nexusengine.core.portal.engagement.dto.*;
import com.nexusengine.core.portal.engagement.service.*;

import com.nexusengine.core.domain.ordering.entity.CustomerOrder;

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
import com.nexusengine.core.domain.customer.entity.Member;

import com.nexusengine.core.domain.ordering.entity.ReturnRequest;
import com.nexusengine.core.domain.ordering.repository.ReturnRequestRepository;
import com.nexusengine.core.domain.ordering.repository.OrderLineRepository;
import com.nexusengine.core.domain.ordering.repository.CustomerOrderRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


/**
 * Order return apply Service implementation
 */
@Service("portalReturnRequestServiceImpl")
@RequiredArgsConstructor
public class ReturnRequestServiceImpl implements ReturnRequestService {
    private final ReturnRequestRepository returnApplyRepository;

    private final OrderLineRepository orderItemRepository;

    private final CustomerOrderRepository orderRepository;

    private final MemberService memberService;

    @Override
    public int create(ReturnApplyParam returnApply) {
        Member currentMember = memberService.getCurrentMember();
        if (returnApply.orderId() == null) {
            return 0;
        }
        CustomerOrder order = orderRepository.findById(returnApply.orderId()).orElse(null);
        if (order == null || !order.getMemberId().equals(currentMember.getId())) {
             throw new RuntimeException("Unauthorized to apply return for this order");
        }

        ReturnRequest realApply = new ReturnRequest();
        BeanUtils.copyProperties(returnApply, realApply);
        realApply.setCreateTime(java.time.OffsetDateTime.now());
        realApply.setStatus(0);
        returnApplyRepository.save(realApply);
        return 1;
    }
}
