package com.nexusengine.core.portal.ordering.dao;

import com.nexusengine.core.domain.ordering.entity.ReturnRequest;
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
import com.nexusengine.core.domain.ordering.repository.ReturnRequestRepository;

import com.nexusengine.core.domain.ordering.entity.CustomerOrder;
import com.nexusengine.core.domain.ordering.entity.OrderLine;
import com.nexusengine.core.domain.catalog.entity.ProductSku;
import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.ordering.repository.OrderLineRepository;
import com.nexusengine.core.domain.ordering.repository.CustomerOrderRepository;
import com.nexusengine.core.domain.catalog.repository.ProductSkuRepository;
import com.nexusengine.core.domain.catalog.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Calendar;
import java.time.LocalDateTime;
import java.util.List;


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
                ProductSku skuStock = skuStockRepository.findById(item.getProductSkuId()).orElse(null);
                if (skuStock != null) {
                    skuStock.setStock(skuStock.getStock() - item.getProductQuantity());
                    skuStock.setLockStock(Math.max(0, skuStock.getLockStock() - item.getProductQuantity()));
                    skuStockRepository.save(skuStock);
                }
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
                ProductSku skuStock = skuStockRepository.findById(item.getProductSkuId()).orElse(null);
                if (skuStock != null) {
                    skuStock.setLockStock(Math.max(0, skuStock.getLockStock() - item.getProductQuantity()));
                    skuStockRepository.save(skuStock);
                }
            }
            
        }
    }
}
