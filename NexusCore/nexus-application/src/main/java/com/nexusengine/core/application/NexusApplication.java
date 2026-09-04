package com.nexusengine.core.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = "com.nexusengine.core")
@EnableMongoRepositories(basePackages = {"com.nexusengine.core.portal.repository"})
public class NexusApplication {
    public static void main(String[] args) {
        SpringApplication.run(NexusApplication.class, args);
    }
}
