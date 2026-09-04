package com.nexusengine.core.ai.config;

import org.springframework.context.annotation.Configuration;

/**
 * AI module auto-configuration marker.
 * Spring AI auto-configuration (ChatClient, OpenAI connection) is handled
 * by spring-ai-openai-spring-boot-starter automatically.
 * This class exists as an anchor for component scanning.
 */
@Configuration
public class AiAutoConfiguration {
}
