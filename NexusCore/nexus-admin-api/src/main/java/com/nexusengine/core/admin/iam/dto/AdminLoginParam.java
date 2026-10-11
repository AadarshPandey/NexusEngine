package com.nexusengine.core.admin.iam.dto;

import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.validation.constraints.NotEmpty;


/**
 * Represents the AdminLoginParam component.
 * Provides core functionality and operations for AdminLoginParam.
 */
public record AdminLoginParam(
    @NotEmpty @Schema(title =  "Username",requiredMode = Schema.RequiredMode.REQUIRED) String username,
    @NotEmpty @Schema(title =  "Password",requiredMode = Schema.RequiredMode.REQUIRED) String password
) {
}
