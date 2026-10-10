package com.nexusengine.core.admin.ordering.dto;

import io.swagger.v3.oas.annotations.media.Schema;

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
import com.nexusengine.core.admin.catalog.dto.*;
import com.nexusengine.core.admin.catalog.service.*;
import com.nexusengine.core.admin.analytics.service.*;
import com.nexusengine.core.admin.ordering.dto.*;
import com.nexusengine.core.admin.ordering.service.*;
import com.nexusengine.core.admin.promotion.dto.*;
import com.nexusengine.core.admin.promotion.service.*;
import com.nexusengine.core.admin.ai.service.*;
import com.nexusengine.core.admin.infra.dto.*;
import com.nexusengine.core.admin.infra.service.*;
import com.nexusengine.core.admin.iam.dto.*;
import com.nexusengine.core.admin.iam.service.*;
import lombok.Getter;
import lombok.Setter;


/**
 * Represents the OrderReceiverInfoParam component.
 * Provides core functionality and operations for OrderReceiverInfoParam.
 */
public record OrderReceiverInfoParam(
    @Schema(title =  "Order id") Long orderId,
    @Schema(title =  "Receiver name") String receiverName,
    @Schema(title =  "Receiver phone") String receiverPhone,
    @Schema(title =  "Receiver post code") String receiverPostCode,
    @Schema(title =  "Receiver detail address") String receiverDetailAddress,
    @Schema(title =  "Receiver province") String receiverProvince,
    @Schema(title =  "Receiver city") String receiverCity,
    @Schema(title =  "Receiver region") String receiverRegion,
    @Schema(title =  "Status") Integer status
) {
}
