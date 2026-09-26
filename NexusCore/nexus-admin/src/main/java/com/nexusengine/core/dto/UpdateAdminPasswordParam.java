package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotEmpty;

/**
 * Represents the UpdateAdminPasswordParam component.
 * Provides core functionality and operations for UpdateAdminPasswordParam.
 */
public record UpdateAdminPasswordParam(
    @NotEmpty @Schema(title =  "Username", requiredMode = Schema.RequiredMode.REQUIRED) String username,
    @NotEmpty @Schema(title =  "Old password", requiredMode = Schema.RequiredMode.REQUIRED) String oldPassword,
    @NotEmpty @Schema(title =  "New password", requiredMode = Schema.RequiredMode.REQUIRED) String newPassword
) {
}
