package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.NotEmpty;

/**
 * Represents the UmsAdminLoginParam component.
 * Provides core functionality and operations for UmsAdminLoginParam.
 */
public record UmsAdminLoginParam(
    @NotEmpty @Schema(title =  "Username",requiredMode = Schema.RequiredMode.REQUIRED) String username,
    @NotEmpty @Schema(title =  "Password",requiredMode = Schema.RequiredMode.REQUIRED) String password
) {
}
