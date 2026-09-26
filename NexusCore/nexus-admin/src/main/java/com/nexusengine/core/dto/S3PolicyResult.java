package com.nexusengine.core.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * S3 presigned URL result
 */
public record S3PolicyResult(
    String presignedUrl,
    String objectKey,
    String bucketName
) {
}
