package com.nexusengine.core.admin.infra.dto;

import io.swagger.v3.oas.annotations.media.Schema;


import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * Represents the MinioUploadDto component.
 * Provides core functionality and operations for MinioUploadDto.
 */
public record MinioUploadDto(
    @Schema(title = "Url") String url,
    @Schema(title = "Name") String name
) {
}
