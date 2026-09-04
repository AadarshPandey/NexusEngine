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
@Table(name = "oms_order_item")
public class OmsOrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    @Schema(title = "Order id")
    private Long orderId;

    @Column(name = "order_sn")
    @Schema(title = "Order sn")
    private String orderSn;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_pic")
    private String productPic;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_brand")
    private String productBrand;

    @Column(name = "product_sn")
    private String productSn;

    @Column(name = "product_price")
    @Schema(title = "Product price")
    private BigDecimal productPrice;

    @Column(name = "product_quantity")
    @Schema(title = "Product quantity")
    private Integer productQuantity;

    @Column(name = "product_sku_id")
    @Schema(title = "Product sku id")
    private Long productSkuId;

    @Column(name = "product_sku_code")
    @Schema(title = "Product sku code")
    private String productSkuCode;

    @Column(name = "product_category_id")
    @Schema(title = "Product category id")
    private Long productCategoryId;

    @Column(name = "promotion_name")
    @Schema(title = "Promotion name")
    private String promotionName;

    @Column(name = "promotion_amount")
    @Schema(title = "Promotion amount")
    private BigDecimal promotionAmount;

    @Column(name = "coupon_amount")
    @Schema(title = "Coupon amount")
    private BigDecimal couponAmount;

    @Column(name = "integration_amount")
    @Schema(title = "Integration amount")
    private BigDecimal integrationAmount;

    @Column(name = "real_amount")
    @Schema(title = "Real amount")
    private BigDecimal realAmount;

    @Column(name = "gift_integration")
    private Integer giftIntegration;

    @Column(name = "gift_growth")
    private Integer giftGrowth;

    @Column(name = "product_attr")
    @Schema(title = "Product attr")
    private String productAttr;
}
