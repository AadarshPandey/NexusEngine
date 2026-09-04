package com.nexusengine.core.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * S3 presigned URL result
 */
@Getter
@Setter
public class S3PolicyResult {
    private String presignedUrl;
    private String objectKey;
    private String bucketName;
}
