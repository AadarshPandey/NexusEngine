package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents the OssCallbackParam component.
 * Provides core functionality and operations for OssCallbackParam.
 */
@Data
@EqualsAndHashCode
public class OssCallbackParam {
    @Schema(title = "Callback url")
    private String callbackUrl;
    @Schema(title = "Callback body")
    private String callbackBody;
    @Schema(title = "Callback body type")
    private String callbackBodyType;
}
