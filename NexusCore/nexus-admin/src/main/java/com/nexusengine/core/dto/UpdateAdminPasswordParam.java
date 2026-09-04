package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotEmpty;

/**
 * Auto-generated documentation
 * Created by macro on 2019/10/9.
 */
@Getter
@Setter
public class UpdateAdminPasswordParam {
    @NotEmpty
    @Schema(title =  "Username", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;
    @NotEmpty
    @Schema(title =  "Old password", requiredMode = Schema.RequiredMode.REQUIRED)
    private String oldPassword;
    @NotEmpty
    @Schema(title =  "New password", requiredMode = Schema.RequiredMode.REQUIRED)
    private String newPassword;
}
