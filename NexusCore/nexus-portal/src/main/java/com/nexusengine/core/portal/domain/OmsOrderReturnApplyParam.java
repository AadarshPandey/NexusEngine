package com.nexusengine.core.portal.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Auto-generated documentation
 * Created by macro on 2018/10/17.
 */
@Getter
@Setter
public class OmsOrderReturnApplyParam {
    @Schema(title = "Order id")
    private Long orderId;
    @Schema(title = "Product id")
    private Long productId;
    @Schema(title = "Order sn")
    private String orderSn;
    @Schema(title = "Member username")
    private String memberUsername;
    @Schema(title = "Return name")
    private String returnName;
    @Schema(title = "Return phone")
    private String returnPhone;
    @Schema(title = "Product pic")
    private String productPic;
    @Schema(title = "Product name")
    private String productName;
    @Schema(title = "Product brand")
    private String productBrand;
    @Schema(title = "Product attr")
    private String productAttr;
    @Schema(title = "Product count")
    private Integer productCount;
    @Schema(title = "Product price")
    private BigDecimal productPrice;
    @Schema(title = "Product real price")
    private BigDecimal productRealPrice;
    @Schema(title = "Reason")
    private String reason;
    @Schema(title = "Description")
    private String description;
    @Schema(title = "Proof pics")
    private String proofPics;

}
