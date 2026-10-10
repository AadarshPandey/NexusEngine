package com.nexusengine.core.portal.ordering.dto;

import com.nexusengine.core.domain.catalog.entity.Product;
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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;


/**
 * Represents the ReturnApplyParam component.
 * Provides core functionality and operations for ReturnApplyParam.
 */
public record ReturnApplyParam(
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
