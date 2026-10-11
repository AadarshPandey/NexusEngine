package com.nexusengine.core.portal.ordering.service;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.domain.ordering.entity.CustomerOrder;

import com.nexusengine.core.domain.customer.entity.Member;

import com.nexusengine.core.domain.ordering.entity.ReturnRequest;
import com.nexusengine.core.domain.ordering.repository.ReturnRequestRepository;
import com.nexusengine.core.domain.ordering.repository.OrderLineRepository;
import com.nexusengine.core.domain.ordering.repository.CustomerOrderRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.nexusengine.core.portal.customer.service.MemberService;
import com.nexusengine.core.portal.ordering.dto.ReturnApplyParam;


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
