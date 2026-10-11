package com.nexusengine.core.admin.ai.controller;

import lombok.RequiredArgsConstructor;

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
@RequiredArgsConstructor
public class AdminChatController {

    @org.springframework.beans.factory.annotation.Qualifier("adminChatServiceImpl")
    private NexusChatService chatService;

    @Operation(summary = "Ask the admin AI assistant a question")
    @PostMapping
    public CommonResult<ChatResponse> chat(@RequestBody ChatRequest request) {
        String answer;
        if (request.sessionId() != null && !request.sessionId().isBlank()) {
            answer = chatService.askWithContext(request.question(), request.sessionId());
        } else {
            answer = chatService.ask(request.question());
        }
        return CommonResult.success(new ChatResponse(request.question(), answer, request.sessionId()));
    }
}
