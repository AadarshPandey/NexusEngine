package com.nexusengine.core.portal.ordering.dto;

import com.nexusengine.core.domain.promotion.entity.Coupon;
import com.nexusengine.core.portal.catalog.dto.*;
import com.nexusengine.core.portal.catalog.service.*;
import com.nexusengine.core.portal.catalog.dao.*;
import com.nexusengine.core.portal.payment.service.*;
import com.nexusengine.core.portal.ordering.dto.*;
import com.nexusengine.core.portal.ordering.service.*;
import com.nexusengine.core.portal.ordering.dao.*;
import com.nexusengine.core.portal.customer.dto.*;
import com.nexusengine.core.portal.customer.service.*;
import com.nexusengine.core.portal.promotion.dto.*;
import com.nexusengine.core.portal.promotion.service.*;
import com.nexusengine.core.portal.promotion.strategy.*;
import com.nexusengine.core.portal.ai.service.*;
import com.nexusengine.core.portal.home.dto.*;
import com.nexusengine.core.portal.home.service.*;
import com.nexusengine.core.portal.engagement.dto.*;
import com.nexusengine.core.portal.engagement.service.*;

import com.nexusengine.core.domain.catalog.entity.*;
import com.nexusengine.core.domain.ordering.entity.*;
import com.nexusengine.core.domain.customer.entity.*;
import com.nexusengine.core.domain.promotion.entity.*;
import com.nexusengine.core.domain.iam.entity.*;
import com.nexusengine.core.domain.catalog.repository.*;
import com.nexusengine.core.domain.ordering.repository.*;
import com.nexusengine.core.domain.customer.repository.*;
import com.nexusengine.core.domain.promotion.repository.*;
import com.nexusengine.core.domain.iam.repository.*;
import com.nexusengine.core.domain.customer.entity.Member;

import com.nexusengine.core.domain.customer.entity.MemberAddress;
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
    private List<MemberAddress> memberReceiveAddressList;
    @Schema(title = "Coupon history detail list")
    private List<CouponHistoryDetail> couponHistoryDetailList;
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
