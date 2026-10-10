package com.nexusengine.core.admin.catalog.dto;

import com.nexusengine.core.domain.catalog.entity.Product;

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

import com.nexusengine.core.admin.shared.validator.FlagValidator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;


/**
 * Represents the CategoryParam component.
 * Provides core functionality and operations for CategoryParam.
 */
public record CategoryParam(
    @Schema(title = "Parent id") Long parentId,
    @NotEmpty @Schema(title = "Name",requiredMode = Schema.RequiredMode.REQUIRED) String name,
    @com.fasterxml.jackson.annotation.JsonProperty("unitOfMeasure") @Schema(title = "Product unit") String productUnit,
    @com.fasterxml.jackson.annotation.JsonProperty("isInNavigation") @FlagValidator(value = {"0","1"},message = "Message") @Schema(title = "Nav status") Integer navStatus,
    @com.fasterxml.jackson.annotation.JsonProperty("isVisible") @FlagValidator(value = {"0","1"},message = "Message") @Schema(title = "Show status") Integer showStatus,
    @com.fasterxml.jackson.annotation.JsonProperty("displayOrder") @Min(value = 0) @Schema(title = "Sort") Integer sort,
    @com.fasterxml.jackson.annotation.JsonProperty("iconUrl") @Schema(title = "Icon") String icon,
    @com.fasterxml.jackson.annotation.JsonProperty("searchKeywords") @Schema(title = "Keywords") String keywords,
    @Schema(title = "Description") String description,
    @Schema(title = "Product attribute id list") List<Long> productAttributeIdList
) {
}
