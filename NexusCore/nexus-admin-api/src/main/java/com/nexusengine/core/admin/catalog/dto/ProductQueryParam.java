package com.nexusengine.core.admin.catalog.dto;

import com.nexusengine.core.domain.catalog.entity.Brand;


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
