package com.nexusengine.core.portal.ordering.service;

import com.nexusengine.core.domain.customer.entity.Member;
import com.nexusengine.core.domain.ordering.entity.CustomerOrder;
import com.nexusengine.core.domain.ordering.entity.OrderPolicy;
import com.nexusengine.core.domain.ordering.entity.ReturnRequest;
import com.nexusengine.core.domain.ordering.repository.CustomerOrderRepository;
import com.nexusengine.core.domain.ordering.repository.OrderPolicyRepository;
import com.nexusengine.core.domain.ordering.repository.ReturnRequestRepository;
import com.nexusengine.core.portal.customer.service.MemberService;
import com.nexusengine.core.portal.ordering.dto.ReturnApplyParam;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Order return apply Service implementation
 */
@Service("portalReturnRequestServiceImpl")
@RequiredArgsConstructor
public class ReturnRequestServiceImpl implements ReturnRequestService {
    private final ReturnRequestRepository returnApplyRepository;
    private final CustomerOrderRepository orderRepository;
    private final OrderPolicyRepository orderPolicyRepository;
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

        // Enforce return window days from order policy
        OrderPolicy policy = orderPolicyRepository.findById(1L).orElse(null);
        int returnWindowDays = (policy != null && policy.getAutoCompleteDays() != null) ? policy.getAutoCompleteDays() : 7;
        if (order.getReceiveTime() != null && order.getReceiveTime().plusDays(returnWindowDays).isBefore(OffsetDateTime.now())) {
            throw new RuntimeException("The return request window (" + returnWindowDays + " days) has expired for this order");
        }

        ReturnRequest realApply = new ReturnRequest();
        realApply.setOrderId(returnApply.orderId());
        realApply.setProductId(returnApply.productId());
        realApply.setOrderSn(returnApply.orderSn() != null ? returnApply.orderSn() : order.getOrderSn());
        realApply.setMemberUsername(currentMember.getUsername());
        realApply.setReturnName(returnApply.returnName() != null ? returnApply.returnName() : currentMember.getDisplayName());
        realApply.setReturnPhone(returnApply.returnPhone() != null ? returnApply.returnPhone() : currentMember.getPhone());
        realApply.setProductPic(returnApply.productPic());
        realApply.setProductName(returnApply.productName());
        realApply.setProductBrand(returnApply.productBrand());
        realApply.setProductAttr(returnApply.productAttr());
        realApply.setProductCount(returnApply.productCount() != null ? returnApply.productCount() : 1);
        realApply.setProductPrice(returnApply.productPrice());
        realApply.setProductRealPrice(returnApply.productRealPrice() != null ? returnApply.productRealPrice() : returnApply.productPrice());
        realApply.setReason(returnApply.reason());
        realApply.setDescription(returnApply.description());
        realApply.setProofPics(returnApply.proofPics());

        BigDecimal refundAmt = (realApply.getProductRealPrice() != null && realApply.getProductCount() != null)
                ? realApply.getProductRealPrice().multiply(BigDecimal.valueOf(realApply.getProductCount()))
                : BigDecimal.ZERO;
        realApply.setReturnAmount(refundAmt);
        realApply.setCreateTime(OffsetDateTime.now());
        realApply.setStatus(0);
        returnApplyRepository.save(realApply);
        return 1;
    }
}

