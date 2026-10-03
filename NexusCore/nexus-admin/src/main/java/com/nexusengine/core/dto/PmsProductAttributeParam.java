package com.nexusengine.core.dto;

import com.nexusengine.core.validator.FlagValidator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.NotEmpty;

/**
 * Represents the PmsProductAttributeParam component.
 * Provides core functionality and operations for PmsProductAttributeParam.
 */
public record PmsProductAttributeParam(
    @NotEmpty @Schema(title = "Product attribute category id") Long productAttributeCategoryId,
    @NotEmpty @Schema(title = "Name") String name,
    @FlagValidator({"0","1","2"}) @Schema(title = "Select type") Integer selectType,
    @FlagValidator({"0","1"}) @Schema(title = "Input type") Integer inputType,
    @Schema(title = "Input list") String inputList,
    Integer sort,
    @Schema(title = "Type") @FlagValidator({"0","1"}) Integer type
) {
}
