package com.nexusengine.core.ai.memory;

import com.nexusengine.core.model.AiChatConversation;
import com.nexusengine.core.repository.AiChatConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JpaChatMemory implements ChatMemory {

    private final AiChatConversationRepository conversationRepository;

    @Override
    public void add(String conversationId, List<Message> messages) {
        for (Message msg : messages) {
            AiChatConversation conv = new AiChatConversation();
            conv.setSessionId(conversationId);
            conv.setRole(msg.getMessageType().getValue());
            conv.setContent(msg.getText());
            conv.setCreateTime(new Date());
            conversationRepository.save(conv);
        }
    }

    @Override
    public List<Message> get(String conversationId) {
        List<AiChatConversation> records = conversationRepository.findBySessionIdOrderByCreateTimeAsc(conversationId);
        
        List<Message> messages = new ArrayList<>();
        for (AiChatConversation rec : records) {
            if (MessageType.USER.getValue().equalsIgnoreCase(rec.getRole())) {
                messages.add(new UserMessage(rec.getContent()));
            } else if (MessageType.ASSISTANT.getValue().equalsIgnoreCase(rec.getRole())) {
                messages.add(new AssistantMessage(rec.getContent()));
            } else if (MessageType.SYSTEM.getValue().equalsIgnoreCase(rec.getRole())) {
                messages.add(new SystemMessage(rec.getContent()));
            }
        }
        return messages;
    }

    @Override
    public void clear(String conversationId) {
        List<AiChatConversation> records = conversationRepository.findBySessionIdOrderByCreateTimeAsc(conversationId);
        conversationRepository.deleteAll(records);
    }
}
