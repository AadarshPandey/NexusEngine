package com.nexusengine.core.domain.ordering.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.domain.catalog.entity.Category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_line", schema = "ordering")
public class OrderLine {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

private Long id;

        @Column(name = "order_id")
    @Schema(title = "Order id")

private Long orderId;
        @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)

private CustomerOrder order;

        @Column(name = "product_id")

private Long productId;
        @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)

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
        @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)

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

    public String getProductImageUrl() {
        return productPic;
    }

    public String getBrandName() {
        return productBrand;
    }

    public BigDecimal getUnitPrice() {
        return productPrice;
    }

    public Integer getQuantity() {
        return productQuantity;
    }

    public BigDecimal getLineTotalAmount() {
        return realAmount;
    }

    public String getSkuAttributes() {
        return productAttr;
    }

    @PrePersist
    protected void onCreate() {
        if (productPrice == null) {
            productPrice = BigDecimal.ZERO;
        }
        if (productQuantity == null) {
            productQuantity = 1;
        }
        if (promotionAmount == null) {
            promotionAmount = BigDecimal.ZERO;
        }
        if (couponAmount == null) {
            couponAmount = BigDecimal.ZERO;
        }
        if (realAmount == null) {
            realAmount = BigDecimal.ZERO;
        }
    }
}
