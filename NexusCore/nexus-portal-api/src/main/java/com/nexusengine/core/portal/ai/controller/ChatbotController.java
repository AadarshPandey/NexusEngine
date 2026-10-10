package com.nexusengine.core.portal.ai.controller;

import com.nexusengine.core.ai.dto.ChatRequest;
import com.nexusengine.core.portal.catalog.dto.*;
import com.nexusengine.core.portal.catalog.service.*;
import com.nexusengine.core.portal.catalog.dao.*;
import com.nexusengine.core.portal.payment.service.*;
import com.nexusengine.core.portal.ordering.dto.*;
import com.nexusengine.core.portal.ordering.service.*;
import com.nexusengine.core.portal.ordering.dao.*;
import com.nexusengine.core.portal.customer.dto.*;
import com.nexusengine.core.portal.customer.service.*;
import com.nexusengine.core.portal.promotion.dto.*;
import com.nexusengine.core.portal.promotion.service.*;
import com.nexusengine.core.portal.promotion.strategy.*;
import com.nexusengine.core.portal.ai.service.*;
import com.nexusengine.core.portal.home.dto.*;
import com.nexusengine.core.portal.home.service.*;
import com.nexusengine.core.portal.engagement.dto.*;
import com.nexusengine.core.portal.engagement.service.*;

import com.nexusengine.core.domain.catalog.entity.*;
import com.nexusengine.core.domain.ordering.entity.*;
import com.nexusengine.core.domain.customer.entity.*;
import com.nexusengine.core.domain.promotion.entity.*;
import com.nexusengine.core.domain.iam.entity.*;
import com.nexusengine.core.domain.catalog.repository.*;
import com.nexusengine.core.domain.ordering.repository.*;
import com.nexusengine.core.domain.customer.repository.*;
import com.nexusengine.core.domain.promotion.repository.*;
import com.nexusengine.core.domain.iam.repository.*;
import com.nexusengine.core.ai.dto.ChatResponse;
import com.nexusengine.core.ai.service.NexusChatService;
import com.nexusengine.core.common.api.CommonResult;
import com.nexusengine.core.domain.ai.entity.ChatMessage;
import com.nexusengine.core.domain.ai.repository.ChatMessageRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Portal AI Chatbot Controller - customer support assistant
 */
@RestController
@Tag(name = "ChatbotController", description = "AI Customer Support Chatbot")
@RequestMapping("/portal/chat")
public class ChatbotController {

    private final NexusChatService chatService;
    private final ChatMessageRepository conversationRepository;

    public ChatbotController(@Qualifier("portalChatServiceImpl") NexusChatService chatService,
                             ChatMessageRepository conversationRepository) {
        this.chatService = chatService;
        this.conversationRepository = conversationRepository;
    }

    @Operation(summary = "Ask the customer support chatbot a question")
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
    
    @Operation(summary = "Get conversation history by session ID")
    @GetMapping("/history")
    public CommonResult<List<ChatMessage>> getHistory(@RequestParam("slotId") String sessionId) {
        return CommonResult.success(conversationRepository.findBySessionIdOrderByCreateTimeAsc(sessionId));
    }
}
