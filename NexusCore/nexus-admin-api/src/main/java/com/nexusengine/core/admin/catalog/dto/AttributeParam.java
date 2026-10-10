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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nexusengine.core.admin.shared.validator.FlagValidator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.NotEmpty;


/**
 * Represents the AttributeParam component.
 * Provides core functionality and operations for AttributeParam.
 */
public record AttributeParam(
    @NotEmpty @Schema(title = "Product attribute category id") Long productAttributeCategoryId,
    @NotEmpty @Schema(title = "Name") String name,
    @JsonProperty("selectionMode") @FlagValidator({"0","1","2"}) @Schema(title = "Select type") Integer selectType,
    @JsonProperty("inputMode") @FlagValidator({"0","1"}) @Schema(title = "Input type") Integer inputType,
    @JsonProperty("allowedValues") @Schema(title = "Input list") String inputList,
    @Schema(title = "Type") @FlagValidator({"0","1"}) Integer type
) {
}
