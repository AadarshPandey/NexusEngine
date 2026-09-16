package com.nexusengine.core.portal.service.impl;

import com.nexusengine.core.model.OmsOrderReturnApply;
import com.nexusengine.core.portal.domain.OmsOrderReturnApplyParam;
import com.nexusengine.core.portal.service.OmsPortalOrderReturnApplyService;
import com.nexusengine.core.repository.OmsOrderReturnApplyRepository;
import com.nexusengine.core.repository.OmsOrderItemRepository;
import com.nexusengine.core.repository.OmsOrderRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Order return apply Service implementation
 */
@Service
@lombok.RequiredArgsConstructor
public class OmsPortalOrderReturnApplyServiceImpl implements OmsPortalOrderReturnApplyService {
    private final OmsOrderReturnApplyRepository returnApplyRepository;

    private final OmsOrderItemRepository orderItemRepository;

    private final OmsOrderRepository orderRepository;

    private final com.nexusengine.core.portal.service.UmsMemberService memberService;

    @Override
    public int create(OmsOrderReturnApplyParam returnApply) {
        com.nexusengine.core.model.UmsMember currentMember = memberService.getCurrentMember();
        if (returnApply.getOrderId() == null) {
            return 0;
        }
        com.nexusengine.core.model.OmsOrder order = orderRepository.findById(returnApply.getOrderId()).orElse(null);
        if (order == null || !order.getMemberId().equals(currentMember.getId())) {
             throw new RuntimeException("Unauthorized to apply return for this order");
        }

        OmsOrderReturnApply realApply = new OmsOrderReturnApply();
        BeanUtils.copyProperties(returnApply, realApply);
        realApply.setCreateTime(new Date());
        realApply.setStatus(0);
        returnApplyRepository.save(realApply);
        return 1;
    }
}
