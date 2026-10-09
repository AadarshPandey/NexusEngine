package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "quantity_tier_discount", schema = "promotion")
public class PmsProductVolumeDiscount implements Serializable {

    private static final long serialVersionUID = 1L;

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
private Long id;

        @Column(name = "product_id")
    
private Long productId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "product_id", insertable = false, updatable = false)
    
private PmsProduct product;

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
