package com.nexusengine.core.ai.service;

/**
 * Base chat service interface shared by all AI-enabled modules.
 * Each module implements this with its own system prompt and knowledge domain.
 */
public interface NexusChatService {
    /**
     * Answer a one-shot question (stateless)
     */
    String ask(String question);

    /**
     * Answer with conversation memory tracked by session ID
     */
    String askWithContext(String question, String sessionId);
}
