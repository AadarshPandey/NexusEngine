package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Auto-generated documentation
 * Created by macro on 2019/12/25.
 */
@Data
@EqualsAndHashCode
public class MinioUploadDto {
    @Schema(title = "Url")
    private String url;
    @Schema(title = "Name")
    private String name;
}
