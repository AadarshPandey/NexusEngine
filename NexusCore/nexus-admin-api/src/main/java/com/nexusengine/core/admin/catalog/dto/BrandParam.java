package com.nexusengine.core.admin.catalog.dto;

import com.nexusengine.core.admin.shared.validator.FlagValidator;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;


/**
 * Represents the BrandParam component.
 * Provides core functionality and operations for BrandParam.
 */
public record BrandParam(
    @NotEmpty @Schema(title =  "Name",requiredMode = Schema.RequiredMode.REQUIRED) String name,
    @FlagValidator(value = {"0","1"}, message = "Message") @Schema(title =  "Visible status") Integer isVisible,
    @NotEmpty @Schema(title =  "Logo",requiredMode = Schema.RequiredMode.REQUIRED) String logo
) {
}
