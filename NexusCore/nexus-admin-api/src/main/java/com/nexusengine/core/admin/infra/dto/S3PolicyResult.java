package com.nexusengine.core.admin.infra.dto;





/**
 * S3 presigned URL result
 */
public record S3PolicyResult(
    String presignedUrl,
    String objectKey,
    String bucketName
) {
}
