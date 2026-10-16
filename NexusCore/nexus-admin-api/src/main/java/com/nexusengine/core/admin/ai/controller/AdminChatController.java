package com.nexusengine.core.admin.ai.controller;

import com.nexusengine.core.ai.dto.ChatRequest;
import com.nexusengine.core.ai.dto.ChatResponse;
import com.nexusengine.core.ai.service.NexusChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "AI - Admin Assistant", description = "AI admin assistant APIs")
@RequestMapping("/api/admin/ai/chat")
@RequiredArgsConstructor
public class AdminChatController {

    @Qualifier("adminChatServiceImpl")
    private NexusChatService chatService;

    @Operation(summary = "Ask the admin AI assistant a question")
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
}
