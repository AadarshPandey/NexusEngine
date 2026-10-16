package com.nexusengine.core.portal.ai.controller;

import com.nexusengine.core.ai.dto.ChatRequest;
import com.nexusengine.core.ai.dto.ChatResponse;
import com.nexusengine.core.ai.service.NexusChatService;
import com.nexusengine.core.domain.ai.entity.ChatMessage;
import com.nexusengine.core.domain.ai.repository.ChatMessageRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Storefront - AI Chatbot", description = "AI customer support chatbot APIs")
@RequestMapping("/portal/chat")
public class ChatbotController {

    private final NexusChatService chatService;
    private final ChatMessageRepository conversationRepository;

    public ChatbotController(
            @Qualifier("portalChatServiceImpl") NexusChatService chatService,
            ChatMessageRepository conversationRepository) {
        this.chatService = chatService;
        this.conversationRepository = conversationRepository;
    }

    @Operation(summary = "Ask the customer support chatbot a question")
    @PostMapping
    public ChatResponse chat(@Valid @RequestBody ChatRequest request) {
        String answer;
        if (request.sessionId() != null && !request.sessionId().isBlank()) {
            answer = chatService.askWithContext(request.question(), request.sessionId());
        } else {
            answer = chatService.ask(request.question());
        }
        return new ChatResponse(request.question(), answer, request.sessionId());
    }

    @Operation(summary = "Get conversation history by session ID")
    @GetMapping("/history")
    public List<ChatMessage> getHistory(@RequestParam("slotId") String sessionId) {
        return conversationRepository.findBySessionIdOrderByCreateTimeAsc(sessionId);
    }
}
