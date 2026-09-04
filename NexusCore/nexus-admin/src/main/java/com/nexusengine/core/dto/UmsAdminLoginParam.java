package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotEmpty;

/**
 * Auto-generated documentation
 * Created by macro on 2018/4/26.
 */
@Data
@EqualsAndHashCode
public class UmsAdminLoginParam {
    @NotEmpty
    @Schema(title =  "Username",requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;
    @NotEmpty
    @Schema(title =  "Password",requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
