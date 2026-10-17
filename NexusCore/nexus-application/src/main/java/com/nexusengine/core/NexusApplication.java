package com.nexusengine.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.nexusengine.core.domain")
@EntityScan(basePackages = "com.nexusengine.core.domain")
@EnableElasticsearchRepositories(basePackages = "com.nexusengine.core.search.repository")
public class NexusApplication {
    public static void main(String[] args) {
        SpringApplication.run(NexusApplication.class, args);
    }
}
