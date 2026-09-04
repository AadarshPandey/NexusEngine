package com.nexusengine.core.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Chat response DTO shared across modules
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    private String question;
    private String answer;
    private String sessionId;

    public static ChatResponse of(String question, String answer) {
        return new ChatResponse(question, answer, null);
    }

    public static ChatResponse of(String question, String answer, String sessionId) {
        return new ChatResponse(question, answer, sessionId);
    }
}
