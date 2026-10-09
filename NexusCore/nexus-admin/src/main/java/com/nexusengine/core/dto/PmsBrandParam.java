package com.nexusengine.core.dto;

import com.nexusengine.core.validator.FlagValidator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

/**
 * Represents the PmsBrandParam component.
 * Provides core functionality and operations for PmsBrandParam.
 */
public record PmsBrandParam(
    @NotEmpty @Schema(title =  "Name",requiredMode = Schema.RequiredMode.REQUIRED) String name,
    @FlagValidator(value = {"0","1"}, message = "Message") @Schema(title =  "Visible status") Integer isVisible,
    @NotEmpty @Schema(title =  "Logo",requiredMode = Schema.RequiredMode.REQUIRED) String logo
) {
}
