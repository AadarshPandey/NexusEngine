package com.nexusengine.core.ai.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Chat request DTO shared across modules
 */
@Getter
@Setter
public class ChatRequest {
    /**
     * The user's question
     */
    private String question;
    /**
     * Optional session ID for multi-turn conversations
     */
    private String sessionId;
}
