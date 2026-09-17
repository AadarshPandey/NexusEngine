package com.nexusengine.core.portal.domain;

import com.nexusengine.core.model.UmsMemberReceiveAddress;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * Represents the ConfirmOrderResult component.
 * Provides core functionality and operations for ConfirmOrderResult.
 */
@Getter
@Setter
public class ConfirmOrderResult {
    @Schema(title = "Cart promotion item list")
    private List<CartPromotionItem> cartPromotionItemList;
    @Schema(title = "Member receive address list")
    private List<UmsMemberReceiveAddress> memberReceiveAddressList;
    @Schema(title = "Coupon history detail list")
    private List<SmsCouponHistoryDetail> couponHistoryDetailList;
    @Schema(title = "Member integration")
    private Integer memberPoints;
    @Schema(title = "Calc amount")
    private CalcAmount calcAmount;

    @Getter
    @Setter
    public static class CalcAmount{
        @Schema(title = "Total amount")
        private BigDecimal totalAmount;
        @Schema(title = "Freight amount")
        private BigDecimal freightAmount;
        @Schema(title = "Promotion amount")
        private BigDecimal promotionAmount;
        @Schema(title = "Pay amount")
        private BigDecimal payAmount;
    }
}
