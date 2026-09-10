package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents the MinioUploadDto component.
 * Provides core functionality and operations for MinioUploadDto.
 */
@Data
@EqualsAndHashCode
public class MinioUploadDto {
    @Schema(title = "Url")
    private String url;
    @Schema(title = "Name")
    private String name;
}
