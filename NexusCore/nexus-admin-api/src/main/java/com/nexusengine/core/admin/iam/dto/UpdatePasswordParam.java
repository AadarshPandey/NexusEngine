package com.nexusengine.core.admin.iam.dto;

import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.validation.constraints.NotEmpty;


/**
 * Represents the UpdatePasswordParam component.
 * Provides core functionality and operations for UpdatePasswordParam.
 */
public record UpdatePasswordParam(
    @NotEmpty @Schema(title =  "Username", requiredMode = Schema.RequiredMode.REQUIRED) String username,
    @NotEmpty @Schema(title =  "Old password", requiredMode = Schema.RequiredMode.REQUIRED) String oldPassword,
    @NotEmpty @Schema(title =  "New password", requiredMode = Schema.RequiredMode.REQUIRED) String newPassword
) {
}
