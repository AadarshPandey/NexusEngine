package com.nexusengine.core.ai.dto;


/**
 * Chat response DTO shared across modules
 */


public record ChatResponse(
    String question,
    String answer,
    String sessionId
) {
}
