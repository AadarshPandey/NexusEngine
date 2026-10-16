package com.nexusengine.core.domain.promotion.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.nexusengine.core.domain.catalog.entity.Category;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "coupon_category", schema = "promotion")
public class CouponCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coupon_id")
    private Long couponId;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", insertable = false, updatable = false)
    private Coupon coupon;

    @Column(name = "product_category_id")
    private Long productCategoryId;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_category_id", insertable = false, updatable = false)
    private Category productCategory;
}
