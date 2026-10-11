package com.nexusengine.core.portal.ordering.dao;

import com.nexusengine.core.domain.ordering.entity.ReturnRequest;

import com.nexusengine.core.domain.ordering.repository.ReturnRequestRepository;

import com.nexusengine.core.domain.ordering.entity.CustomerOrder;
import com.nexusengine.core.domain.ordering.entity.OrderLine;
import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.ordering.repository.OrderLineRepository;
import com.nexusengine.core.domain.ordering.repository.CustomerOrderRepository;
import com.nexusengine.core.domain.catalog.repository.ProductSkuRepository;
import com.nexusengine.core.domain.catalog.repository.ProductRepository;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import com.nexusengine.core.portal.ordering.dto.OrderDetail;


/**
 * Portal order data access - replaces legacy MyBatis OrderDao
 */
@Repository
@RequiredArgsConstructor
public class OrderDao {
    private final CustomerOrderRepository orderRepository;
    private final OrderLineRepository orderItemRepository;
    private final ProductSkuRepository skuStockRepository;

    private final ProductRepository productRepository;
    private final ReturnRequestRepository returnApplyRepository;

    public OrderDetail getDetail(Long orderId) {
        CustomerOrder order = orderRepository.findById(orderId).orElse(null);
        if (order == null) return null;

        List<OrderLine> orderItemList = orderItemRepository.findByOrderId(orderId);
        List<ReturnRequest> applyList = returnApplyRepository.findByOrderIdIn(java.util.Collections.singletonList(orderId));
        return new OrderDetail(order, orderItemList, applyList);
    }

    public List<OrderDetail> getTimeOutOrders(Integer minutes) {
        java.time.LocalDateTime deadline = java.time.LocalDateTime.now().minusMinutes(minutes);


        List<CustomerOrder> orders = orderRepository.findByStatusAndDeleteStatusIsNullAndCreateTimeBefore(0, deadline);
        List<OrderDetail> result = new ArrayList<>();
        for (CustomerOrder order : orders) {

            List<OrderLine> orderItemList = orderItemRepository.findByOrderId(order.getId());
            List<ReturnRequest> applyList = returnApplyRepository.findByOrderIdIn(java.util.Collections.singletonList(order.getId()));
            result.add(new OrderDetail(order, orderItemList, applyList));
        }
        return result;
    }

    public void updateOrderStatus(List<Long> ids, int status) {
        for (Long id : ids) {
            CustomerOrder order = orderRepository.findById(id).orElse(null);
            if (order != null) {
                order.setStatus(status);
                orderRepository.save(order);
            }
        }
    }

    public int updateSkuStock(List<OrderLine> orderItemList) {
        int count = 0;
        for (OrderLine item : orderItemList) {
            if (item.getProductSkuId() != null) {
                skuStockRepository.deductStockOnPayment(item.getProductSkuId(), item.getProductQuantity());
            }
            Product product = productRepository.findById(item.getProductId()).orElse(null);
            if (product != null) {
                product.setSale((product.getSale() == null ? 0 : product.getSale()) + item.getProductQuantity());
                productRepository.save(product);
                count++;
            }
        }
        return count;
    }

    public void releaseSkuStockLock(List<OrderLine> orderItemList) {
        for (OrderLine item : orderItemList) {
            if (item.getProductSkuId() != null) {
                skuStockRepository.releaseReservedStock(item.getProductSkuId(), item.getProductQuantity());
            }
        }
    }
}
