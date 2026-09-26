package com.nexusengine.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents the OssPolicyResult component.
 * Provides core functionality and operations for OssPolicyResult.
 */
public record OssPolicyResult(
    @Schema(title = "Access key id") String accessKeyId,
    @Schema(title = "Policy") String policy,
    @Schema(title = "Signature") String signature,
    @Schema(title = "Dir") String dir,
    @Schema(title = "Host") String host,
    @Schema(title = "Callback") String callback
) {
}
