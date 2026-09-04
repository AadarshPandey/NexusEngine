package com.nexusengine.core.portal.dao;

import com.nexusengine.core.model.OmsOrder;
import com.nexusengine.core.model.OmsOrderItem;
import com.nexusengine.core.model.PmsSkuStock;
import com.nexusengine.core.portal.domain.OmsOrderDetail;
import com.nexusengine.core.repository.OmsOrderItemRepository;
import com.nexusengine.core.repository.OmsOrderRepository;
import com.nexusengine.core.repository.PmsSkuStockRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Portal order data access - replaces legacy MyBatis PortalOrderDao
 */
@Repository
public class PortalOrderDao {
    @Autowired
    private OmsOrderRepository orderRepository;
    @Autowired
    private OmsOrderItemRepository orderItemRepository;
    @Autowired
    private PmsSkuStockRepository skuStockRepository;

    public OmsOrderDetail getDetail(Long orderId) {
        OmsOrder order = orderRepository.findById(orderId).orElse(null);
        if (order == null) return null;
        OmsOrderDetail detail = new OmsOrderDetail();
        BeanUtils.copyProperties(order, detail);
        detail.setOrderItemList(orderItemRepository.findByOrderId(orderId));
        return detail;
    }

    public List<OmsOrderDetail> getTimeOutOrders(Integer minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -minutes);
        Date deadline = calendar.getTime();
        // Find orders: status=0 (unpaid), deleteStatus=0, created before deadline
        List<OmsOrder> orders = orderRepository.findAll();
        List<OmsOrderDetail> result = new ArrayList<>();
        for (OmsOrder order : orders) {
            if (order.getStatus() == 0 && order.getDeleteStatus() == 0
                    && order.getCreateTime() != null && order.getCreateTime().before(deadline)) {
                OmsOrderDetail detail = new OmsOrderDetail();
                BeanUtils.copyProperties(order, detail);
                detail.setOrderItemList(orderItemRepository.findByOrderId(order.getId()));
                result.add(detail);
            }
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
                    count++;
                }
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
