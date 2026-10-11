package com.nexusengine.core.admin.infra.service;


import jakarta.servlet.http.HttpServletRequest;

import com.nexusengine.core.admin.infra.dto.S3CallbackResult;
import com.nexusengine.core.admin.infra.dto.S3PolicyResult;


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
