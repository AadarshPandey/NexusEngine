package com.nexusengine.core.service.impl;

import com.nexusengine.core.dto.S3CallbackResult;
import com.nexusengine.core.dto.S3PolicyResult;
import com.nexusengine.core.service.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import jakarta.servlet.http.HttpServletRequest;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * S3 service implementation
 */
@Service
@lombok.RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {
    private static final Logger LOGGER = LoggerFactory.getLogger(S3ServiceImpl.class);

    private final S3Presigner s3Presigner;

    @Value("${spring.cloud.aws.s3.bucket-name:nexus-storage}")
    private String bucketName;
    @Value("${spring.cloud.aws.s3.policy.expire:300}")
    private int expireSeconds;
    @Value("${spring.cloud.aws.s3.dir.prefix:nexus/images/}")
    private String dirPrefix;
    @Value("${spring.cloud.aws.s3.endpoint:}")
    private String endpoint;

    @Override
    public S3PolicyResult presign() {
        // Generate unique object key
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyyMMdd");
        String objectKey = dirPrefix + java.time.LocalDateTime.now().format(sdf) + "/" + UUID.randomUUID() + ".jpg";

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(expireSeconds))
                .putObjectRequest(objectRequest)
                .build();

        var presignedUrl = s3Presigner.presignPutObject(presignRequest).url().toString();

        return new S3PolicyResult(presignedUrl, objectKey, bucketName);
    }

    @Override
    public S3CallbackResult callback(HttpServletRequest request) {
        String objectKey = request.getParameter("key");
        String url = endpoint + "/" + bucketName + "/" + objectKey;
        return new S3CallbackResult(objectKey, request.getParameter("size"), request.getParameter("mimeType"), url);
    }
}
