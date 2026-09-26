package com.nexusengine.core.portal.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

/**
 * Represents the OmsOrderReturnApplyParam component.
 * Provides core functionality and operations for OmsOrderReturnApplyParam.
 */
public record OmsOrderReturnApplyParam(
    @Schema(title = "Order id") Long orderId,
    @Schema(title = "Product id") Long productId,
    @Schema(title = "Order sn") String orderSn,
    @Schema(title = "Member username") String memberUsername,
    @Schema(title = "Return name") String returnName,
    @Schema(title = "Return phone") String returnPhone,
    @Schema(title = "Product pic") String productPic,
    @Schema(title = "Product name") String productName,
    @Schema(title = "Product brand") String productBrand,
    @Schema(title = "Product attr") String productAttr,
    @Schema(title = "Product count") Integer productCount,
    @Schema(title = "Product price") BigDecimal productPrice,
    @Schema(title = "Product real price") BigDecimal productRealPrice,
    @Schema(title = "Reason") String reason,
    @Schema(title = "Description") String description,
    @Schema(title = "Proof pics") String proofPics
) {
}
