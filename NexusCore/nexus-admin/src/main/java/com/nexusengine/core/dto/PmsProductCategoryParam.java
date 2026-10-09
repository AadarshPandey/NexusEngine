package com.nexusengine.core.dto;

import com.nexusengine.core.validator.FlagValidator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Represents the PmsProductCategoryParam component.
 * Provides core functionality and operations for PmsProductCategoryParam.
 */
public record PmsProductCategoryParam(
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
