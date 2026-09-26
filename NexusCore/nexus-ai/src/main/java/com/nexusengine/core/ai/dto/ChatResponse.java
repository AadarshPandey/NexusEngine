package com.nexusengine.core.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Chat response DTO shared across modules
 */


public record ChatResponse(
    String question,
    String answer,
    String sessionId
) {
}
