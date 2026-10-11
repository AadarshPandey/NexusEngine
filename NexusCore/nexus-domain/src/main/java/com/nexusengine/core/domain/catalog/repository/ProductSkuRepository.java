package com.nexusengine.core.domain.catalog.repository;

import com.nexusengine.core.domain.catalog.entity.ProductSku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductSkuRepository extends JpaRepository<ProductSku, Long> {
    List<ProductSku> findByProductId(Long productId);
    List<ProductSku> findByProductIdAndSkuCodeContaining(Long productId, String skuCode);
    void deleteByProductId(Long productId);

    @Modifying
    @Query("UPDATE ProductSku s SET s.reservedQty = COALESCE(s.reservedQty, 0) + :quantity WHERE s.id = :skuId AND (COALESCE(s.onHandQty, 0) - COALESCE(s.reservedQty, 0)) >= :quantity")
    int reserveStock(@Param("skuId") Long skuId, @Param("quantity") Integer quantity);

    @Modifying
    @Query("UPDATE ProductSku s SET s.reservedQty = CASE WHEN COALESCE(s.reservedQty, 0) >= :quantity THEN s.reservedQty - :quantity ELSE 0 END WHERE s.id = :skuId")
    int releaseReservedStock(@Param("skuId") Long skuId, @Param("quantity") Integer quantity);

    @Modifying
    @Query("UPDATE ProductSku s SET s.onHandQty = s.onHandQty - :quantity, s.reservedQty = CASE WHEN COALESCE(s.reservedQty, 0) >= :quantity THEN s.reservedQty - :quantity ELSE 0 END WHERE s.id = :skuId AND s.onHandQty >= :quantity")
    int deductStockOnPayment(@Param("skuId") Long skuId, @Param("quantity") Integer quantity);

    @Modifying
    @Query("UPDATE ProductSku s SET s.unitsSold = COALESCE(s.unitsSold, 0) + :quantity WHERE s.id = :skuId")
    int incrementUnitsSold(@Param("skuId") Long skuId, @Param("quantity") Integer quantity);
}
