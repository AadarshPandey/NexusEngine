package com.nexusengine.core.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nexusengine.core")
public class NexusSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(NexusSearchApplication.class, args);
    }
}
