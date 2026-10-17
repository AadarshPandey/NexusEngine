package com.nexusengine.core.admin.catalog.dto;




import io.swagger.v3.oas.annotations.media.Schema;


/**
 * Represents the ProductQueryParam component.
 * Provides core functionality and operations for ProductQueryParam.
 */
public record ProductQueryParam(
        @Schema(title = "Publish status") Integer isPublished,
        @Schema(title = "Approval status") Integer approvalStatus,
        @Schema(title = "Keyword") String keyword,
        @Schema(title = "Product code") String productCode,
        @Schema(title = "Product category id") Long productCategoryId,
        @Schema(title = "Brand id") Long brandId) {}
