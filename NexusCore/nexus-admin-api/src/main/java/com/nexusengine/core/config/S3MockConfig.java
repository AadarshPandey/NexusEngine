package com.nexusengine.core.config;

import org.springframework.context.annotation.Bean;


import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.regions.Region;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import java.net.URI;


@Configuration
public class S3MockConfig {
    @Bean
    @ConditionalOnMissingBean(S3Client.class)
    public S3Client s3Client() {
        return S3Client.builder()
            .region(Region.US_EAST_1)
            .endpointOverride(URI.create("http://localhost:9000"))
            .build();
    }
}
