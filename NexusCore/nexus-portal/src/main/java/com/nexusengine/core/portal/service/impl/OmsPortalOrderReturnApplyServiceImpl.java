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
public class OmsPortalOrderReturnApplyServiceImpl implements OmsPortalOrderReturnApplyService {
    @Autowired
    private OmsOrderReturnApplyRepository returnApplyRepository;

    @Autowired
    private OmsOrderItemRepository orderItemRepository;

    @Autowired
    private OmsOrderRepository orderRepository;

    @Override
    public int create(OmsOrderReturnApplyParam returnApply) {
        OmsOrderReturnApply realApply = new OmsOrderReturnApply();
        BeanUtils.copyProperties(returnApply, realApply);
        realApply.setCreateTime(new Date());
        realApply.setStatus(0);
        returnApplyRepository.save(realApply);

        // Check if all items in the order have been fully returned
        Long orderId = realApply.getOrderId();
        if (orderId != null) {
            java.util.List<com.nexusengine.core.model.OmsOrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
            java.util.List<OmsOrderReturnApply> applies = returnApplyRepository.findByOrderId(orderId);

            int totalOrderQty = orderItems.stream().mapToInt(i -> i.getProductQuantity() != null ? i.getProductQuantity() : 0).sum();
            int totalReturnedQty = applies.stream().mapToInt(a -> a.getProductCount() != null ? a.getProductCount() : 0).sum();

            if (totalOrderQty > 0 && totalReturnedQty >= totalOrderQty) {
                com.nexusengine.core.model.OmsOrder order = orderRepository.findById(orderId).orElse(null);
                if (order != null) {
                    order.setStatus(6); // Set status to Refunded
                    orderRepository.save(order);
                }
            }
        }

        return 1;
    }
}
