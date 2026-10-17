package com.nexusengine.core.admin.ordering.service;

import lombok.RequiredArgsConstructor;


import com.nexusengine.core.admin.ordering.dto.OrderDeliveryParam;
import com.nexusengine.core.admin.ordering.dto.OrderDetail;
import com.nexusengine.core.admin.ordering.dto.OrderMoneyInfoParam;
import com.nexusengine.core.admin.ordering.dto.OrderQueryParam;
import com.nexusengine.core.admin.ordering.dto.OrderReceiverInfoParam;



import com.nexusengine.core.domain.catalog.repository.BrandRepository;
import com.nexusengine.core.domain.catalog.repository.ProductRepository;
import com.nexusengine.core.domain.ordering.repository.CustomerOrderRepository;
import com.nexusengine.core.domain.ordering.repository.OrderLineRepository;
import com.nexusengine.core.domain.ordering.repository.OrderStatusHistoryRepository;
import com.nexusengine.core.domain.ordering.entity.CustomerOrder;
import com.nexusengine.core.domain.ordering.entity.OrderStatusHistory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service("adminOrderServiceImpl")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
    private final CustomerOrderRepository orderRepository;
    private final OrderLineRepository orderItemRepository;
    private final OrderStatusHistoryRepository orderOperateHistoryRepository;
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final com.nexusengine.core.admin.security.AdminTenantContext adminTenantContext;
    private final com.nexusengine.core.domain.payment.repository.PaymentTransactionRepository paymentTransactionRepository;

    @Override
    public List<CustomerOrder> list(OrderQueryParam queryParam, Integer pageSize, Integer pageNum) {
        return orderRepository.findAll((root, query, cb) -> {
            var predicates = new java.util.ArrayList<jakarta.persistence.criteria.Predicate>();
            if (queryParam != null) {
                if (org.springframework.util.StringUtils.hasText(queryParam.orderSn())) {
                    predicates.add(cb.equal(root.get("orderSn"), queryParam.orderSn()));
                }
                if (queryParam.status() != null) {
                    predicates.add(cb.equal(root.get("status"), queryParam.status()));
                }
                if (org.springframework.util.StringUtils.hasText(queryParam.receiverKeyword())) {
                    predicates.add(cb.or(
                            cb.like(root.get("receiverName"), "%" + queryParam.receiverKeyword() + "%"),
                            cb.like(root.get("receiverPhone"), "%" + queryParam.receiverKeyword() + "%")
                    ));
                }
                if (queryParam.orderType() != null) {
                    predicates.add(cb.equal(root.get("orderType"), queryParam.orderType()));
                }
            }
            adminTenantContext.currentBrandId().ifPresent(brandId -> {
                jakarta.persistence.criteria.Subquery<Long> subquery = query.subquery(Long.class);
                jakarta.persistence.criteria.Root<com.nexusengine.core.domain.ordering.entity.OrderLine> lineRoot =
                        subquery.from(com.nexusengine.core.domain.ordering.entity.OrderLine.class);
                subquery.select(lineRoot.get("orderId"));
                subquery.where(
                        cb.equal(lineRoot.get("orderId"), root.get("id")),
                        cb.equal(lineRoot.get("product").get("brandId"), brandId)
                );
                predicates.add(cb.exists(subquery));
            });
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        }, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createTime"));
    }

    private void assertOrderBrandAccess(Long orderId) {
        adminTenantContext.currentBrandId().ifPresent(brandId -> {
            String brandName = brandRepository.findById(brandId)
                    .map(com.nexusengine.core.domain.catalog.entity.Brand::getName)
                    .orElse(null);
            boolean hasBrandLine = orderItemRepository.findByOrderId(orderId).stream()
                    .anyMatch(l -> (brandName != null && brandName.equalsIgnoreCase(l.getProductBrand()))
                            || (l.getProductId() != null && productRepository.findById(l.getProductId())
                                .map(p -> brandId.equals(p.getBrandId())).orElse(false)));
            if (!hasBrandLine) {
                throw new com.nexusengine.core.common.exception.ApiException(com.nexusengine.core.common.api.ResultCode.FORBIDDEN);
            }
        });
    }

    @Override
    @Transactional
    public int delivery(List<OrderDeliveryParam> deliveryParamList) {
        int count = 0;
        for (OrderDeliveryParam param : deliveryParamList) {
            assertOrderBrandAccess(param.orderId());
            CustomerOrder order = orderRepository.findById(param.orderId()).orElse(null);
            if (order != null) {
                order.setDeliverySn(param.deliverySn());
                order.setDeliveryCompany(param.deliveryCompany());
                order.setDeliveryTime(java.time.OffsetDateTime.now());
                order.setStatus(2);
                orderRepository.save(order);
                count++;
            }
        }
        List<OrderStatusHistory> historyList = deliveryParamList.stream().map(p -> {
            OrderStatusHistory h = new OrderStatusHistory();
            h.setOrderId(p.orderId());
            h.setCreateTime(java.time.OffsetDateTime.now());
            h.setOperateMan("Admin");
            h.setOrderStatus(2);
            h.setNote("Delivery completed");
            return h;
        }).collect(Collectors.toList());
        orderOperateHistoryRepository.saveAll(historyList);
        return count;
    }

    @Override
    @Transactional
    public int close(List<Long> ids, String note) {
        for (Long id : ids) {
            assertOrderBrandAccess(id);
        }
        List<CustomerOrder> orders = orderRepository.findAllById(ids);
        for (CustomerOrder order : orders) {
            order.setStatus(4);
            orderRepository.save(order);
        }
        List<OrderStatusHistory> historyList = ids.stream().map(id -> {
            OrderStatusHistory h = new OrderStatusHistory();
            h.setOrderId(id);
            h.setCreateTime(java.time.OffsetDateTime.now());
            h.setOperateMan("Admin");
            h.setOrderStatus(4);
            h.setNote("Order closed: " + note);
            return h;
        }).collect(Collectors.toList());
        orderOperateHistoryRepository.saveAll(historyList);
        return orders.size();
    }

    @Override
    @Transactional
    public int delete(List<Long> ids) {
        for (Long id : ids) {
            assertOrderBrandAccess(id);
        }
        List<CustomerOrder> orders = orderRepository.findAllById(ids);
        for (CustomerOrder order : orders) {
            order.setDeletedAt(java.time.Instant.now());
            orderRepository.save(order);
        }
        return orders.size();
    }

    @Override
    public OrderDetail detail(Long id) {
        CustomerOrder order = orderRepository.findById(id).orElse(null);
        if (order == null) return null;
        assertOrderBrandAccess(id);
        List<com.nexusengine.core.domain.ordering.entity.OrderLine> lines = orderItemRepository.findByOrderId(id);
        if (adminTenantContext.isBrandScoped()) {
            Long brandId = adminTenantContext.currentBrandId().get();
            String brandName = brandRepository.findById(brandId)
                    .map(com.nexusengine.core.domain.catalog.entity.Brand::getName)
                    .orElse(null);
            lines = lines.stream().filter(l -> (brandName != null && brandName.equalsIgnoreCase(l.getProductBrand()))
                    || (l.getProductId() != null && productRepository.findById(l.getProductId())
                        .map(p -> brandId.equals(p.getBrandId())).orElse(false))).collect(Collectors.toList());
        }
        List<com.nexusengine.core.domain.payment.entity.PaymentTransaction> txns =
                paymentTransactionRepository.findByOrderIdOrderByCreateTimeAsc(id);
        return new OrderDetail(order, lines, orderOperateHistoryRepository.findByOrderId(id), txns);
    }

    @Override
    @Transactional
    public int updateReceiverInfo(OrderReceiverInfoParam receiverInfoParam) {
        if (receiverInfoParam != null && receiverInfoParam.orderId() != null) {
            assertOrderBrandAccess(receiverInfoParam.orderId());
        }
        CustomerOrder order = orderRepository.findById(receiverInfoParam.orderId()).orElse(null);
        if (order == null) return 0;
        order.setReceiverName(receiverInfoParam.receiverName());
        order.setReceiverPhone(receiverInfoParam.receiverPhone());
        order.setReceiverPostCode(receiverInfoParam.receiverPostCode());
        order.setReceiverDetailAddress(receiverInfoParam.receiverDetailAddress());

        order.setModifyTime(java.time.OffsetDateTime.now());
        orderRepository.save(order);
        OrderStatusHistory h = new OrderStatusHistory();
        h.setOrderId(receiverInfoParam.orderId());
        h.setCreateTime(java.time.OffsetDateTime.now());
        h.setOperateMan("Admin");
        h.setOrderStatus(receiverInfoParam.status());
        h.setNote("Modified receiver info");
        orderOperateHistoryRepository.save(h);
        return 1;
    }

    @Override
    @Transactional
    public int updateMoneyInfo(OrderMoneyInfoParam moneyInfoParam) {
        if (moneyInfoParam != null && moneyInfoParam.orderId() != null) {
            assertOrderBrandAccess(moneyInfoParam.orderId());
        }
        CustomerOrder order = orderRepository.findById(moneyInfoParam.orderId()).orElse(null);
        if (order == null) return 0;
        order.setFreightAmount(moneyInfoParam.freightAmount());
        order.setDiscountAmount(moneyInfoParam.discountAmount());
        order.setModifyTime(java.time.OffsetDateTime.now());
        orderRepository.save(order);
        OrderStatusHistory h = new OrderStatusHistory();
        h.setOrderId(moneyInfoParam.orderId());
        h.setCreateTime(java.time.OffsetDateTime.now());
        h.setOperateMan("Admin");
        h.setOrderStatus(moneyInfoParam.status());
        h.setNote("Modified fee info");
        orderOperateHistoryRepository.save(h);
        return 1;
    }

    @Override
    @Transactional
    public int updateNote(Long id, String note, Integer status) {
        assertOrderBrandAccess(id);
        CustomerOrder order = orderRepository.findById(id).orElse(null);
        if (order == null) return 0;
        order.setNote(note);
        order.setModifyTime(java.time.OffsetDateTime.now());
        orderRepository.save(order);
        OrderStatusHistory h = new OrderStatusHistory();
        h.setOrderId(id);
        h.setCreateTime(java.time.OffsetDateTime.now());
        h.setOperateMan("Admin");
        h.setOrderStatus(status);
        h.setNote("Modified note: " + note);
        orderOperateHistoryRepository.save(h);
        return 1;
    }

    @Override
    @Transactional
    public int updateStatus(List<Long> ids, Integer status, String note) {
        for (Long id : ids) {
            assertOrderBrandAccess(id);
        }
        List<CustomerOrder> orders = orderRepository.findAllById(ids);
        for (CustomerOrder order : orders) {
            order.setStatus(status);
            order.setModifyTime(java.time.OffsetDateTime.now());
            orderRepository.save(order);
        }
        List<OrderStatusHistory> historyList = ids.stream().map(id -> {
            OrderStatusHistory h = new OrderStatusHistory();
            h.setOrderId(id);
            h.setCreateTime(java.time.OffsetDateTime.now());
            h.setOperateMan("Admin");
            h.setOrderStatus(status);
            h.setNote(note != null ? note : "Status updated by Admin");
            return h;
        }).collect(Collectors.toList());
        orderOperateHistoryRepository.saveAll(historyList);
        return orders.size();
    }
}
