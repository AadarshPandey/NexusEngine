package com.nexusengine.core.ai.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract base implementation of NexusChatService.
 * Provides stateless and session-based chat using Spring AI ChatClient.
 * Subclasses only need to provide their system prompt via {@link #getSystemPrompt()}.
 */
public abstract class AbstractNexusChatService implements NexusChatService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractNexusChatService.class);

    private final ChatClient chatClient;
    private final ChatClient.Builder chatClientBuilder;
    private final Map<String, ChatClient> sessionClients = new ConcurrentHashMap<>();

    protected AbstractNexusChatService(ChatClient.Builder chatClientBuilder) {
        this.chatClientBuilder = chatClientBuilder;
        this.chatClient = chatClientBuilder
                .defaultSystem(getSystemPrompt())
                .build();
    }

    /**
     * Return the system prompt that defines this chatbot's knowledge domain and personality.
     * Each module provides a different prompt.
     */
    protected abstract String getSystemPrompt();

    @Override
    public String ask(String question) {
        try {
            return chatClient.prompt()
                    .user(question)
                    .call()
                    .content();
        } catch (Exception e) {
            LOGGER.error("Chatbot error: {}", e.getMessage());
            return getFallbackMessage();
        }
    }

    @Override
    public String askWithContext(String question, String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            return ask(question);
        }
        try {
            ChatClient sessionClient = sessionClients.computeIfAbsent(sessionId, id -> {
                ChatMemory chatMemory = MessageWindowChatMemory.builder()
                        .chatMemoryRepository(new InMemoryChatMemoryRepository())
                        .maxMessages(20)
                        .build();
                return chatClientBuilder
                        .defaultSystem(getSystemPrompt())
                        .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                        .build();
            });
            return sessionClient.prompt()
                    .user(question)
                    .call()
                    .content();
        } catch (Exception e) {
            LOGGER.error("Chatbot error for session {}: {}", sessionId, e.getMessage());
            return getFallbackMessage();
        }
    }

    /**
     * Override to customize the fallback message on error.
     */
    protected String getFallbackMessage() {
        return "I'm sorry, I'm unable to process your question right now. Please try again later.";
    }
}
