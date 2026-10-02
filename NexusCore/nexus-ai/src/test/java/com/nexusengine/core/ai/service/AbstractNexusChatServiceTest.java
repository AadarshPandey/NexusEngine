package com.nexusengine.core.ai.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.CallResponseSpec;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AbstractNexusChatServiceTest {

    private ChatClient.Builder chatClientBuilder;
    private ChatClient chatClient;
    private ChatClient.ChatClientRequestSpec requestSpec;
    private CallResponseSpec responseSpec;
    private VectorStore vectorStore;
    private ChatMemory chatMemory;
    
    private TestNexusChatService service;

    private static class TestNexusChatService extends AbstractNexusChatService {
        public TestNexusChatService(ChatClient.Builder chatClientBuilder) {
            super(chatClientBuilder);
        }

        @Override
        protected String getSystemPrompt() {
            return "System Prompt";
        }

        public void setVectorStore(VectorStore vectorStore) {
            try {
                java.lang.reflect.Field f = AbstractNexusChatService.class.getDeclaredField("vectorStore");
                f.setAccessible(true);
                f.set(this, vectorStore);
            } catch (Exception e) {
                throw new RuntimeException("Failed to inject vectorStore", e);
            }
        }
    }

    @BeforeEach
    void setUp() {
        chatClientBuilder = mock(ChatClient.Builder.class);
        chatClient = mock(ChatClient.class);
        requestSpec = mock(ChatClient.ChatClientRequestSpec.class);
        responseSpec = mock(CallResponseSpec.class);
        vectorStore = mock(VectorStore.class);
        chatMemory = mock(ChatMemory.class);

        when(chatClientBuilder.defaultSystem(any(String.class))).thenReturn(chatClientBuilder);
        when(chatClientBuilder.defaultAdvisors(any(MessageChatMemoryAdvisor.class))).thenReturn(chatClientBuilder);
        when(chatClientBuilder.build()).thenReturn(chatClient);
        
        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.user(any(String.class))).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(responseSpec);
        when(responseSpec.content()).thenReturn("AI response");

        service = new TestNexusChatService(chatClientBuilder);
    }

    @Test
    void ask_Success() {
        String response = service.ask("Hello");
        assertEquals("AI response", response);
        verify(requestSpec).user("Hello");
    }

    @Test
    void ask_FallbackOnException() {
        when(requestSpec.call()).thenThrow(new RuntimeException("Simulated error"));
        String response = service.ask("Hello");
        assertEquals("I'm sorry, I'm unable to process your question right now. Please try again later.", response);
    }

    @Test
    void askWithContext_NoSessionId() {
        String response = service.askWithContext("Hello", null);
        assertEquals("AI response", response);
        verify(requestSpec).user("Hello");
    }

    @Test
    void askWithContext_WithSessionId_AndVectorStore() {
        service.setVectorStore(vectorStore);
        Document doc = new Document("Context text");
        when(vectorStore.similaritySearch(any(SearchRequest.class))).thenReturn(Collections.singletonList(doc));

        String response = service.askWithContext("Hello", "session-123");
        
        assertEquals("AI response", response);
        
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(requestSpec, times(2)).user(captor.capture());
        String finalQuestion = captor.getValue();
        
        assertEquals("Hello\n\nContext Information:\nContext text", finalQuestion);
    }
}
