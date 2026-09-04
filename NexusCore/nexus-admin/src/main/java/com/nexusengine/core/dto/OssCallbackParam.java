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
public class OssCallbackParam {
    @Schema(title = "Callback url")
    private String callbackUrl;
    @Schema(title = "Callback body")
    private String callbackBody;
    @Schema(title = "Callback body type")
    private String callbackBodyType;
}
