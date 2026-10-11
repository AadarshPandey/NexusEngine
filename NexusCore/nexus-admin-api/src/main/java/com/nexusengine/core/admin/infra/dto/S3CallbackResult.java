package com.nexusengine.core.admin.infra.dto;





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
