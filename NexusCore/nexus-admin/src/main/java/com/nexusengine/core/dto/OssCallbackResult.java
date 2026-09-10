package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents the OssCallbackResult component.
 * Provides core functionality and operations for OssCallbackResult.
 */
@Data
@EqualsAndHashCode
public class OssCallbackResult {
    @Schema(title = "Filename")
    private String filename;
    @Schema(title = "Size")
    private String size;
    @Schema(title = "Mime type")
    private String mimeType;
    @Schema(title = "Width")
    private String width;
    @Schema(title = "Height")
    private String height;
}
