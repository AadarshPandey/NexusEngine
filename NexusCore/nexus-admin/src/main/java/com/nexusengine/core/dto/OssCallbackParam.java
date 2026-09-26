package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents the OssCallbackParam component.
 * Provides core functionality and operations for OssCallbackParam.
 */
public record OssCallbackParam(
    @Schema(title = "Callback url") String callbackUrl,
    @Schema(title = "Callback body") String callbackBody,
    @Schema(title = "Callback body type") String callbackBodyType
) {
}
