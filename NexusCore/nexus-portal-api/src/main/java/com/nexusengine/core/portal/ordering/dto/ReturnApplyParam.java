package com.nexusengine.core.portal.ordering.dto;



import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

/**
 * Represents the ReturnApplyParam component.
 * Provides core functionality and operations for ReturnApplyParam.
 */
public record ReturnApplyParam(
    @Schema(title = "Order id") Long orderId,
    @Schema(title = "Product id") Long productId,
    @JsonAlias({"orderNumber", "order_sn"})
    @Schema(title = "Order sn") String orderSn,
    @Schema(title = "Member username") String memberUsername,
    @JsonAlias({"contactName", "return_name"})
    @Schema(title = "Return name") String returnName,
    @JsonAlias({"contactPhone", "return_phone"})
    @Schema(title = "Return phone") String returnPhone,
    @JsonAlias({"productImageUrl", "product_pic"})
    @Schema(title = "Product pic") String productPic,
    @Schema(title = "Product name") String productName,
    @JsonAlias({"brandName", "product_brand"})
    @Schema(title = "Product brand") String productBrand,
    @JsonAlias({"skuAttributes", "product_attr"})
    @Schema(title = "Product attr") String productAttr,
    @JsonAlias({"quantity", "product_count"})
    @Schema(title = "Product count") Integer productCount,
    @JsonAlias({"unitPrice", "product_price"})
    @Schema(title = "Product price") BigDecimal productPrice,
    @JsonAlias({"unitRefundAmount", "product_real_price"})
    @Schema(title = "Product real price") BigDecimal productRealPrice,
    @Schema(title = "Reason") String reason,
    @JsonAlias({"customerComment", "customer_comment"})
    @Schema(title = "Description") String description,
    @JsonAlias({"evidenceUrls", "proof_pics"})
    @Schema(title = "Proof pics") String proofPics
) {
}
