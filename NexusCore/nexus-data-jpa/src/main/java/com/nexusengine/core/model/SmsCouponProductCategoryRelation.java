package com.nexusengine.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sms_coupon_product_category_relation")
public class SmsCouponProductCategoryRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coupon_id")
    private Long couponId;

    @Column(name = "product_category_id")
    private Long productCategoryId;

    @Column(name = "product_category_name")
    @Schema(title = "Product category name")
    private String productCategoryName;

    @Column(name = "parent_category_name")
    @Schema(title = "Parent category name")
    private String parentCategoryName;
}
