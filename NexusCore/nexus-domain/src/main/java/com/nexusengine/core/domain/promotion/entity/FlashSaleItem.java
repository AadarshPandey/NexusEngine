package com.nexusengine.core.domain.promotion.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.nexusengine.core.domain.catalog.entity.Product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "flash_sale_item", schema = "promotion")
public class FlashSaleItem {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

        @Column(name = "flash_sale_id", nullable = false)

private Long flashPromotionId;
        @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flash_sale_id", insertable = false, updatable = false)

private FlashSale flashPromotion;

        @Column(name = "slot_id", nullable = false)

private Long flashPromotionSessionId;
        @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id", insertable = false, updatable = false)

private FlashSaleSlot flashPromotionSession;

        @Column(name = "product_id", nullable = false)

private Long productId;
        @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)

private Product product;

        @Column(name = "flash_price", nullable = false)

private BigDecimal flashPromotionPrice;

        @Column(name = "quantity_available", nullable = false)

private Integer flashPromotionCount;

        @Column(name = "per_member_limit", nullable = false)
    @JdbcTypeCode(SqlTypes.SMALLINT)
    private Integer flashPromotionLimit = 1;

    @PrePersist
    protected void onCreate() {
        if (flashPromotionLimit == null) {
            flashPromotionLimit = 1;
        }
    }
}
