package com.nexusengine.core.ai.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class AbstractNexusChatService implements NexusChatService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractNexusChatService.class);

    private final ChatClient chatClient;
    private final ChatClient.Builder chatClientBuilder;
    
    @Autowired(required = false)
    private ChatMemory chatMemory;
    
    @Autowired(required = false)
    private VectorStore vectorStore;

    private final Map<String, ChatClient> sessionClients = new ConcurrentHashMap<>();

    protected AbstractNexusChatService(ChatClient.Builder chatClientBuilder) {
        this.chatClientBuilder = chatClientBuilder;
        this.chatClient = chatClientBuilder
                .defaultSystem(getSystemPrompt())
                .build();
    }

    protected abstract String getSystemPrompt();

    @Override
    public String ask(String question) {
        try {
            return chatClient.prompt()
                    .user(question)
                    .call()
                    .content();
        } catch (Exception e) {
            LOGGER.error("Chatbot error: ", e);
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
                ChatClient.Builder builder = chatClientBuilder.defaultSystem(getSystemPrompt());
                if (chatMemory != null) {
                    builder.defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build());
                }
                return builder.build();
            });
            
            String finalQuestion = question;
            if (vectorStore != null) {
                List<Document> docs = vectorStore.similaritySearch(
                    SearchRequest.builder().query(question).topK(2).build()
                );
                if (docs != null && !docs.isEmpty()) {
                    String contextStr = docs.stream().map(Document::getText).collect(Collectors.joining("\n"));
                    finalQuestion = question + "\n\nContext Information:\n" + contextStr;
                }
            }
            
            return sessionClient.prompt()
                    .user(finalQuestion)
                    .advisors(a -> a.param("chat_memory_conversation_id", sessionId))
                    .call()
                    .content();
        } catch (Exception e) {
            LOGGER.error("Chatbot error for session {}: ", sessionId, e);
            return getFallbackMessage();
        }
    }

    protected String getFallbackMessage() {
        return "I'm sorry, I'm unable to process your question right now. Please try again later.";
    }
}
