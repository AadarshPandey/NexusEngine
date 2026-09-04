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
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;

/**
 * S3 service implementation
 */
@Service
public class S3ServiceImpl implements S3Service {
    private static final Logger LOGGER = LoggerFactory.getLogger(S3ServiceImpl.class);

    @Autowired
    private S3Presigner s3Presigner;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;
    @Value("${aws.s3.policy.expire:300}")
    private int expireSeconds;
    @Value("${aws.s3.dir.prefix:nexus/images/}")
    private String dirPrefix;
    @Value("${aws.s3.endpoint:}")
    private String endpoint;

    @Override
    public S3PolicyResult presign() {
        S3PolicyResult result = new S3PolicyResult();
        // Generate unique object key
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String objectKey = dirPrefix + sdf.format(new Date()) + "/" + UUID.randomUUID() + ".jpg";

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(expireSeconds))
                .putObjectRequest(objectRequest)
                .build();

        var presignedUrl = s3Presigner.presignPutObject(presignRequest).url().toString();

        result.setPresignedUrl(presignedUrl);
        result.setObjectKey(objectKey);
        result.setBucketName(bucketName);
        return result;
    }

    @Override
    public S3CallbackResult callback(HttpServletRequest request) {
        S3CallbackResult result = new S3CallbackResult();
        String objectKey = request.getParameter("key");
        String url = endpoint + "/" + bucketName + "/" + objectKey;
        result.setFilename(objectKey);
        result.setSize(request.getParameter("size"));
        result.setMimeType(request.getParameter("mimeType"));
        result.setUrl(url);
        return result;
    }
}
