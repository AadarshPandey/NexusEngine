package com.nexusengine.core.domain.ai.repository;

import com.nexusengine.core.domain.ai.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySessionIdOrderByCreateTimeAsc(String sessionId);
    List<ChatMessage> findBySessionIdOrderByCreateTimeDesc(String sessionId, Pageable pageable);
}
