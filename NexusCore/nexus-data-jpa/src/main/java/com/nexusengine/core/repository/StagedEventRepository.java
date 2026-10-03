package com.nexusengine.core.repository;

import com.nexusengine.core.model.StagedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StagedEventRepository extends JpaRepository<StagedEvent, Long> {
    List<StagedEvent> findByStatusOrderByCreatedAtAsc(String status, org.springframework.data.domain.Pageable pageable);
}
