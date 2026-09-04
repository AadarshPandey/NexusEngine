package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

/**
 * Auto-generated documentation
 * Created by macro on 2018/4/26.
 */
@Getter
@Setter
public class UmsAdminParam {
    @NotEmpty
    @Schema(title = "Username", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;
    @NotEmpty
    @Schema(title =  "Password", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
    @Schema(title =  "Icon")
    private String icon;
    @Email
    @Schema(title =  "Email")
    private String email;
    @Schema(title =  "Nick name")
    private String nickName;
    @Schema(title =  "Note")
    private String note;
}
