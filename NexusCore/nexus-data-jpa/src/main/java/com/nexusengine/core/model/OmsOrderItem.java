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
@Table(name = "oms_order_item", schema = "orders")
public class OmsOrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    @Schema(title = "Order id")
    private Long orderId;
    @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "order_id", insertable = false, updatable = false)
    private OmsOrder order;

    @Column(name = "product_id")
    private Long productId;
    @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "product_id", insertable = false, updatable = false)
    private PmsProduct product;

    @Column(name = "product_pic")
    private String productPic;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_brand")
    private String productBrand;

    @Column(name = "product_price")
    @Schema(title = "Product price")
    private BigDecimal productPrice;

    @Column(name = "product_quantity")
    @Schema(title = "Product quantity")
    private Integer productQuantity;

    @Column(name = "product_sku_id")
    @Schema(title = "Product sku id")
    private Long productSkuId;

    @Column(name = "product_category_id")
    @Schema(title = "Product category id")
    private Long productCategoryId;
    @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "product_category_id", insertable = false, updatable = false)
    private PmsProductCategory productCategory;

    @Column(name = "promotion_name")
    @Schema(title = "Promotion name")
    private String promotionName;

    @Column(name = "promotion_amount")
    @Schema(title = "Promotion amount")
    private BigDecimal promotionAmount;

    @Column(name = "coupon_amount")
    @Schema(title = "Coupon amount")
    private BigDecimal couponAmount;

    @Column(name = "real_amount")
    @Schema(title = "Real amount")
    private BigDecimal realAmount;

    @Column(name = "product_attr")
    @Schema(title = "Product attr")
    private String productAttr;
}
