package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents the PmsProductQueryParam component.
 * Provides core functionality and operations for PmsProductQueryParam.
 */
public record PmsProductQueryParam(
    @Schema(title = "Publish status") Integer publishStatus,
    @Schema(title = "Verify status") Integer verifyStatus,
    @Schema(title = "Keyword") String keyword,
    @Schema(title = "Product sn") String productSn,
    @Schema(title = "Product category id") Long productCategoryId,
    @Schema(title = "Brand id") Long brandId
) {
}
