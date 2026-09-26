package com.nexusengine.core.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * S3 upload callback result
 */
public record S3CallbackResult(
    String filename,
    String size,
    String mimeType,
    String url
) {
}
