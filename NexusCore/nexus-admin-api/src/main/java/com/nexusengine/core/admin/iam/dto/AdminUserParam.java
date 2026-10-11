package com.nexusengine.core.admin.iam.dto;

import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;


/**
 * Represents the AdminUserParam component.
 * Provides core functionality and operations for AdminUserParam.
 */
public record AdminUserParam(
    @NotEmpty @Schema(title = "Username", requiredMode = Schema.RequiredMode.REQUIRED) String username,
    @NotEmpty @Schema(title =  "Password", requiredMode = Schema.RequiredMode.REQUIRED) String password,
    @Schema(title =  "Icon") String icon,
    @Email @Schema(title =  "Email") String email,
    @Schema(title =  "Nick name") String nickName,
    @Schema(title =  "Note") String note
) {
}
