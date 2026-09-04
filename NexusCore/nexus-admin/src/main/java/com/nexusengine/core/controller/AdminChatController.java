package com.nexusengine.core.controller;

import com.nexusengine.core.ai.dto.ChatRequest;
import com.nexusengine.core.ai.dto.ChatResponse;
import com.nexusengine.core.ai.service.NexusChatService;
import com.nexusengine.core.common.api.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Admin AI Assistant Controller - helps admins with operations, analytics, and management tasks
 */
@RestController
@Tag(name = "AdminChatController", description = "AI Admin Assistant")
@RequestMapping("/chat")
public class AdminChatController {

    @Autowired
    @org.springframework.beans.factory.annotation.Qualifier("adminChatServiceImpl")
    private NexusChatService chatService;

    @Operation(summary = "Ask the admin AI assistant a question")
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
