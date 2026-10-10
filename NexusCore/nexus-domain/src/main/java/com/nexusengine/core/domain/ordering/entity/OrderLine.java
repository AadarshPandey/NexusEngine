package com.nexusengine.core.domain.ordering.entity;

import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.catalog.entity.Category;
import com.nexusengine.core.domain.promotion.entity.Coupon;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_line", schema = "ordering")
public class OrderLine implements Serializable {

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
    
private CustomerOrder order;

        @Column(name = "product_id")
    
private Long productId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "product_id", insertable = false, updatable = false)
    
private Product product;

        @Column(name = "product_image_url")
    
private String productPic;

        @Column(name = "product_name")
    
private String productName;

        @Column(name = "brand_name")
    
private String productBrand;

        @Column(name = "unit_price")
    @Schema(title = "Product price")
    
private BigDecimal productPrice;

        @Column(name = "quantity")
    @Schema(title = "Product quantity")
    
private Integer productQuantity;

        @Column(name = "sku_id")
    @Schema(title = "Product sku id")
    
private Long productSkuId;

        @Column(name = "category_id")
    @Schema(title = "Product category id")
    
private Long productCategoryId;
        @com.fasterxml.jackson.annotation.JsonIgnore
    @jakarta.persistence.ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @jakarta.persistence.JoinColumn(name = "category_id", insertable = false, updatable = false)
    
private Category productCategory;

        @Column(name = "promotion_name")
    @Schema(title = "Promotion name")
    
private String promotionName;

        @Column(name = "promotion_discount_amount")
    @Schema(title = "Promotion amount")
    
private BigDecimal promotionAmount;

        @Column(name = "coupon_discount_amount")
    @Schema(title = "Coupon amount")
    
private BigDecimal couponAmount;

        @Column(name = "line_total_amount")
    @Schema(title = "Real amount")
    
private BigDecimal realAmount;

        @Column(name = "sku_attributes")
    @Schema(title = "Product attr")
    
private String productAttr;
}
