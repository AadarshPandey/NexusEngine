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
public class OssPolicyResult {
    @Schema(title = "Access key id")
    private String accessKeyId;
    @Schema(title = "Policy")
    private String policy;
    @Schema(title = "Signature")
    private String signature;
    @Schema(title = "Dir")
    private String dir;
    @Schema(title = "Host")
    private String host;
    @Schema(title = "Callback")
    private String callback;
}
