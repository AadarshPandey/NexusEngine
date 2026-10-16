package com.nexusengine.core.domain.promotion.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.nexusengine.core.domain.catalog.entity.Product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "quantity_tier_discount", schema = "promotion")
public class QuantityTierDiscount {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

        @Column(name = "product_id")

private Long productId;
        @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)

private Product product;

            @Schema(title = "Count")
    @Column(name = "min_quantity")
private Integer count;

            @Schema(title = "Discount")
    @Column(name = "discount_rate")
private BigDecimal discount;

            @Schema(title = "Price")
    @Column(name = "tier_unit_price")
private BigDecimal price;
}
