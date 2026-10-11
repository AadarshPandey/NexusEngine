package com.nexusengine.core.ai.dto;


/**
 * Chat request DTO shared across modules
 */
public record ChatRequest(
    String question,
    String sessionId
) {
}
