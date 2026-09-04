package com.nexusengine.core.repository;

import com.nexusengine.core.model.PmsCommentReplay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PmsCommentReplayRepository extends JpaRepository<PmsCommentReplay, Long>, JpaSpecificationExecutor<PmsCommentReplay> {
}
