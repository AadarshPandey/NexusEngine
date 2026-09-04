package com.nexusengine.core.service;

import com.nexusengine.core.dto.S3PolicyResult;
import com.nexusengine.core.dto.S3CallbackResult;

import jakarta.servlet.http.HttpServletRequest;

/**
 * S3 object storage service
 */
public interface S3Service {
    /**
     * Generate a presigned URL for uploading to S3
     */
    S3PolicyResult presign();

    /**
     * Handle upload callback
     */
    S3CallbackResult callback(HttpServletRequest request);
}
