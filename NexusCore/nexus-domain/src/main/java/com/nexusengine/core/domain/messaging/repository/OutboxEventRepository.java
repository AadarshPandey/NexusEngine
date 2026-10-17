package com.nexusengine.core.domain.messaging.repository;

import com.nexusengine.core.domain.messaging.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
    List<OutboxEvent> findByStatusOrderByCreatedAtAsc(String status, org.springframework.data.domain.Pageable pageable);
    Long countByStatus(String status);
    List<OutboxEvent> findTop10ByOrderByCreatedAtDesc();
}
