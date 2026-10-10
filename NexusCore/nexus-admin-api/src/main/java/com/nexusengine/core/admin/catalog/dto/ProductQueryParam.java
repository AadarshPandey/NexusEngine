package com.nexusengine.core.admin.catalog.dto;

import com.nexusengine.core.domain.catalog.entity.Brand;

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
import com.nexusengine.core.domain.catalog.entity.Product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * Represents the ProductQueryParam component.
 * Provides core functionality and operations for ProductQueryParam.
 */
public record ProductQueryParam(
    @Schema(title = "Publish status") @com.fasterxml.jackson.annotation.JsonProperty("isPublished") Integer publishStatus,
    @Schema(title = "Verify status") @com.fasterxml.jackson.annotation.JsonProperty("approvalStatus") Integer verifyStatus,
    @Schema(title = "Keyword") String keyword,
    @Schema(title = "Product sn") @com.fasterxml.jackson.annotation.JsonProperty("productCode") String productSn,
    @Schema(title = "Product category id") Long productCategoryId,
    @Schema(title = "Brand id") Long brandId
) {
}
