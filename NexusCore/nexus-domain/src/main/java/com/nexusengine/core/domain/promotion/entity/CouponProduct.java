package com.nexusengine.core.domain.promotion.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.nexusengine.core.domain.catalog.entity.Product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "coupon_product", schema = "promotion")
public class CouponProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coupon_id")
    private Long couponId;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", insertable = false, updatable = false)
    private Coupon coupon;

    @Column(name = "product_id")
    private Long productId;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;
}
