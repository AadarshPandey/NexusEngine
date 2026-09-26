package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents the OssCallbackResult component.
 * Provides core functionality and operations for OssCallbackResult.
 */
public record OssCallbackResult(
    @Schema(title = "Filename") String filename,
    @Schema(title = "Size") String size,
    @Schema(title = "Mime type") String mimeType,
    @Schema(title = "Width") String width,
    @Schema(title = "Height") String height
) {
}
