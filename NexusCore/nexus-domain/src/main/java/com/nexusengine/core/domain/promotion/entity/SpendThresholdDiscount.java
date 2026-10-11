package com.nexusengine.core.domain.promotion.entity;

import com.nexusengine.core.domain.catalog.entity.Product;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "spend_threshold_discount", schema = "promotion")
public class SpendThresholdDiscount implements Serializable {

    private static final long serialVersionUID = 1L;

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

        @Column(name = "product_id")

private Long productId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "product_id", insertable = false, updatable = false)

private Product product;

        @Column(name = "min_spend_amount")

private BigDecimal fullPrice;

        @Column(name = "discount_amount")

private BigDecimal reducePrice;
}
