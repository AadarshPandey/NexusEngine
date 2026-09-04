package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Auto-generated documentation
 * Created by macro on 2018/5/17.
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
