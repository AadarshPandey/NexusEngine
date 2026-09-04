package com.nexusengine.core.portal.controller;

import com.nexusengine.core.ai.dto.ChatRequest;
import com.nexusengine.core.ai.dto.ChatResponse;
import com.nexusengine.core.ai.service.NexusChatService;
import com.nexusengine.core.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Portal AI Chatbot Controller - customer support assistant
 */
@RestController
@Tag(name = "ChatbotController", description = "AI Customer Support Chatbot")
@RequestMapping("/portal/chat")
public class ChatbotController {

    @Autowired
    @org.springframework.beans.factory.annotation.Qualifier("portalChatServiceImpl")
    private NexusChatService chatService;

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
}
