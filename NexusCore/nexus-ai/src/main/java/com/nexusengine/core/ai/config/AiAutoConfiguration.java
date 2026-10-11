package com.nexusengine.core.ai.config;

import org.springframework.context.annotation.Configuration;

/**
 * AI module auto-configuration marker.
 * Spring AI auto-configuration (ChatClient, Gemini connection) is handled
 * by spring-ai-starter-model-google-genai automatically.
 * This class exists as an anchor for component scanning.
 */
@Configuration
public class AiAutoConfiguration {
}
