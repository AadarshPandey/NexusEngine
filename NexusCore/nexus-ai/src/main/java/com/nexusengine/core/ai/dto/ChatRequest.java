package com.nexusengine.core.ai.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Chat request DTO shared across modules
 */
public record ChatRequest(
    String question,
    String sessionId
) {
}
