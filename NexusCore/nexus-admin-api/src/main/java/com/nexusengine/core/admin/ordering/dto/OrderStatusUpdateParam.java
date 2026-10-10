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
import java.math.BigDecimal;


/**
 * Represents the OrderStatusUpdateParam component.
 * Provides core functionality and operations for OrderStatusUpdateParam.
 */
public record OrderStatusUpdateParam(
    @Schema(title = "Id") Long id,
    @Schema(title = "Company address id") Long companyAddressId,
    @Schema(title = "Return amount") BigDecimal returnAmount,
    @Schema(title = "Handle note") String handleNote,
    @Schema(title = "Handle man") String handleMan,
    @Schema(title = "Receive note") String receiveNote,
    @Schema(title = "Receive man") String receiveMan,
    @Schema(title = "Status") Integer status
) {
}
