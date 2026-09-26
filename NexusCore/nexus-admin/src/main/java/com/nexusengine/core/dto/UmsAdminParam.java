package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

/**
 * Represents the UmsAdminParam component.
 * Provides core functionality and operations for UmsAdminParam.
 */
public record UmsAdminParam(
    @NotEmpty @Schema(title = "Username", requiredMode = Schema.RequiredMode.REQUIRED) String username,
    @NotEmpty @Schema(title =  "Password", requiredMode = Schema.RequiredMode.REQUIRED) String password,
    @Schema(title =  "Icon") String icon,
    @Email @Schema(title =  "Email") String email,
    @Schema(title =  "Nick name") String nickName,
    @Schema(title =  "Note") String note
) {
}
