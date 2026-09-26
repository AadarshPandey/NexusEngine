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
    @Schema(title =  "First letter") String firstLetter,
    @Min(value = 0) @Schema(title =  "Sort") Integer sort,
    @FlagValidator(value = {"0","1"}, message = "Message") @Schema(title =  "Factory status") Integer factoryStatus,
    @FlagValidator(value = {"0","1"}, message = "Message") @Schema(title =  "Show status") Integer showStatus,
    @NotEmpty @Schema(title =  "Logo",requiredMode = Schema.RequiredMode.REQUIRED) String logo,
    @Schema(title =  "Big pic") String bigPic
) {
}
