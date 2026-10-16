package com.nexusengine.core.portal.catalog.service;

import com.nexusengine.core.common.exception.InsufficientStockException;
import com.nexusengine.core.domain.catalog.repository.ProductSkuRepository;
import com.nexusengine.core.domain.ordering.entity.OrderLine;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service handling atomic stock reservations, releases, and payment deductions.
 * Backed by atomic SQL queries directly against catalog.product_sku.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StockReservationService {

    private final ProductSkuRepository skuStockRepository;

    /**
     * Atomically reserve stock for a single SKU.
     * Throws InsufficientStockException if (onHandQty - reservedQty) < quantity.
     */
    @Transactional
    public void reserveStock(Long skuId, int quantity) {
        if (skuId == null || quantity <= 0) {
            return;
        }
        int updated = skuStockRepository.reserveStock(skuId, quantity);
        if (updated == 0) {
            log.warn("Stock reservation failed for SKU {}: insufficient available stock for qty {}", skuId, quantity);
            throw new InsufficientStockException(skuId);
        }
    }

    /**
     * Atomically release previously reserved stock for a single SKU.
     */
    @Transactional
    public void releaseReservedStock(Long skuId, int quantity) {
        if (skuId == null || quantity <= 0) {
            return;
        }
        skuStockRepository.releaseReservedStock(skuId, quantity);
    }

    /**
     * Atomically release reserved stock for a batch of order lines.
     */
    @Transactional
    public void releaseReservedStockBatch(List<OrderLine> orderLines) {
        if (orderLines == null || orderLines.isEmpty()) {
            return;
        }
        for (OrderLine item : orderLines) {
            if (item.getProductSkuId() != null && item.getProductQuantity() != null) {
                skuStockRepository.releaseReservedStock(item.getProductSkuId(), item.getProductQuantity());
            }
        }
    }

    /**
     * Atomically deduct stock from onHandQty and decrement reservedQty on successful payment.
     */
    @Transactional
    public void deductStockOnPayment(Long skuId, int quantity) {
        if (skuId == null || quantity <= 0) {
            return;
        }
        skuStockRepository.deductStockOnPayment(skuId, quantity);
        skuStockRepository.incrementUnitsSold(skuId, quantity);
    }

    /**
     * Atomically deduct stock and increment units sold for a batch of order lines.
     */
    @Transactional
    public void deductStockOnPaymentBatch(List<OrderLine> orderLines) {
        if (orderLines == null || orderLines.isEmpty()) {
            return;
        }
        for (OrderLine item : orderLines) {
            if (item.getProductSkuId() != null && item.getProductQuantity() != null) {
                skuStockRepository.deductStockOnPayment(item.getProductSkuId(), item.getProductQuantity());
                skuStockRepository.incrementUnitsSold(item.getProductSkuId(), item.getProductQuantity());
            }
        }
    }
}
