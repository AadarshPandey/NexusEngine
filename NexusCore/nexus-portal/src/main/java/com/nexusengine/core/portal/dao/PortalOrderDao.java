package com.nexusengine.core.portal.dao;

import com.nexusengine.core.model.OmsOrder;
import com.nexusengine.core.model.OmsOrderItem;
import com.nexusengine.core.model.PmsSkuStock;
import com.nexusengine.core.model.PmsProduct;
import com.nexusengine.core.portal.domain.OmsOrderDetail;
import com.nexusengine.core.repository.OmsOrderItemRepository;
import com.nexusengine.core.repository.OmsOrderRepository;
import com.nexusengine.core.repository.PmsSkuStockRepository;
import com.nexusengine.core.repository.PmsProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Calendar;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Portal order data access - replaces legacy MyBatis PortalOrderDao
 */
@Repository
@RequiredArgsConstructor
public class PortalOrderDao {
    private final OmsOrderRepository orderRepository;
    private final OmsOrderItemRepository orderItemRepository;
    private final PmsSkuStockRepository skuStockRepository;

    private final PmsProductRepository productRepository;
    private final com.nexusengine.core.repository.OmsOrderReturnApplyRepository returnApplyRepository;

    public OmsOrderDetail getDetail(Long orderId) {
        OmsOrder order = orderRepository.findById(orderId).orElse(null);
        if (order == null) return null;
        
        List<OmsOrderItem> orderItemList = orderItemRepository.findByOrderId(orderId);
        List<com.nexusengine.core.model.OmsOrderReturnApply> applyList = returnApplyRepository.findByOrderIdIn(java.util.Collections.singletonList(orderId));
        return new OmsOrderDetail(order, orderItemList, applyList);
    }

    public List<OmsOrderDetail> getTimeOutOrders(Integer minutes) {
        java.time.LocalDateTime deadline = java.time.LocalDateTime.now().minusMinutes(minutes);
        
        
        List<OmsOrder> orders = orderRepository.findByStatusAndDeleteStatusAndCreateTimeBefore(0, 0, deadline);
        List<OmsOrderDetail> result = new ArrayList<>();
        for (OmsOrder order : orders) {
            
            List<OmsOrderItem> orderItemList = orderItemRepository.findByOrderId(order.getId());
            List<com.nexusengine.core.model.OmsOrderReturnApply> applyList = returnApplyRepository.findByOrderIdIn(java.util.Collections.singletonList(order.getId()));
            result.add(new OmsOrderDetail(order, orderItemList, applyList));
        }
        return result;
    }

    public void updateOrderStatus(List<Long> ids, int status) {
        for (Long id : ids) {
            OmsOrder order = orderRepository.findById(id).orElse(null);
            if (order != null) {
                order.setStatus(status);
                orderRepository.save(order);
            }
        }
    }

    public int updateSkuStock(List<OmsOrderItem> orderItemList) {
        int count = 0;
        for (OmsOrderItem item : orderItemList) {
            if (item.getProductSkuId() != null) {
                PmsSkuStock skuStock = skuStockRepository.findById(item.getProductSkuId()).orElse(null);
                if (skuStock != null) {
                    skuStock.setStock(skuStock.getStock() - item.getProductQuantity());
                    skuStock.setLockStock(Math.max(0, skuStock.getLockStock() - item.getProductQuantity()));
                    skuStockRepository.save(skuStock);
                }
            }
            PmsProduct product = productRepository.findById(item.getProductId()).orElse(null);
            if (product != null) {
                product.setSale((product.getSale() == null ? 0 : product.getSale()) + item.getProductQuantity());
                productRepository.save(product);
                count++;
            }
        }
        return count;
    }

    public void releaseSkuStockLock(List<OmsOrderItem> orderItemList) {
        for (OmsOrderItem item : orderItemList) {
            if (item.getProductSkuId() != null) {
                PmsSkuStock skuStock = skuStockRepository.findById(item.getProductSkuId()).orElse(null);
                if (skuStock != null) {
                    skuStock.setLockStock(Math.max(0, skuStock.getLockStock() - item.getProductQuantity()));
                    skuStockRepository.save(skuStock);
                }
            }
            
        }
    }
}
