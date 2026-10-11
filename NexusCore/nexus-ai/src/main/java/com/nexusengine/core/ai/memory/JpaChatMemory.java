package com.nexusengine.core.ai.memory;

import com.nexusengine.core.domain.ai.entity.ChatMessage;
import com.nexusengine.core.domain.ai.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JpaChatMemory implements ChatMemory {

    private final ChatMessageRepository conversationRepository;

    @Override
    public void add(String conversationId, List<Message> messages) {
        for (Message msg : messages) {
            ChatMessage conv = new ChatMessage();
            conv.setSessionId(conversationId);
            conv.setRole(msg.getMessageType().getValue());
            conv.setContent(msg.getText());
            conv.setCreateTime(java.time.OffsetDateTime.now());
            conv.setModel("gemini-2.5-flash");
            if (msg.getText() != null) {
                conv.setTokensUsed(Math.max(1, (msg.getText().length() + 3) / 4));
            }
            conversationRepository.save(conv);
        }
    }

    @Override
    public List<Message> get(String conversationId) {
        List<ChatMessage> records = conversationRepository.findBySessionIdOrderByCreateTimeAsc(conversationId);

        List<Message> messages = new ArrayList<>();
        for (ChatMessage rec : records) {
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
        List<ChatMessage> records = conversationRepository.findBySessionIdOrderByCreateTimeAsc(conversationId);
        conversationRepository.deleteAll(records);
    }
}
