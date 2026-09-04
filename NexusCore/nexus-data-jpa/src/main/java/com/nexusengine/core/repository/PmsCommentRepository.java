package com.nexusengine.core.repository;

import com.nexusengine.core.model.PmsComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PmsCommentRepository extends JpaRepository<PmsComment, Long>, JpaSpecificationExecutor<PmsComment> {
}
