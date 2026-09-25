package com.nexusengine.core.repository;

import com.nexusengine.core.model.AiChatConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AiChatConversationRepository extends JpaRepository<AiChatConversation, Long> {
    List<AiChatConversation> findBySessionIdOrderByCreateTimeAsc(String sessionId);
    List<AiChatConversation> findBySessionIdOrderByCreateTimeDesc(String sessionId, Pageable pageable);
}
