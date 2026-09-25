package com.nexusengine.core.portal.controller;

import com.nexusengine.core.ai.dto.ChatRequest;
import com.nexusengine.core.ai.dto.ChatResponse;
import com.nexusengine.core.ai.service.NexusChatService;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.model.AiChatConversation;
import com.nexusengine.core.repository.AiChatConversationRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Portal AI Chatbot Controller - customer support assistant
 */
@RestController
@Tag(name = "ChatbotController", description = "AI Customer Support Chatbot")
@RequestMapping("/portal/chat")
@RequiredArgsConstructor
public class ChatbotController {

    @Qualifier("portalChatServiceImpl")
    private final NexusChatService chatService;
    
    private final AiChatConversationRepository conversationRepository;

    @Operation(summary = "Ask the customer support chatbot a question")
    @PostMapping
    public CommonResult<ChatResponse> chat(@RequestBody ChatRequest request) {
        String answer;
        if (request.getSessionId() != null && !request.getSessionId().isBlank()) {
            answer = chatService.askWithContext(request.getQuestion(), request.getSessionId());
        } else {
            answer = chatService.ask(request.getQuestion());
        }
        return CommonResult.success(ChatResponse.of(request.getQuestion(), answer, request.getSessionId()));
    }
    
    @Operation(summary = "Get conversation history by session ID")
    @GetMapping("/history")
    public CommonResult<List<AiChatConversation>> getHistory(@RequestParam String sessionId) {
        return CommonResult.success(conversationRepository.findBySessionIdOrderByCreateTimeAsc(sessionId));
    }
}
