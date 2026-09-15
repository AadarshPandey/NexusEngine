package com.nexusengine.core.repository;

import com.nexusengine.core.model.PmsCommentReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PmsCommentReplyRepository extends JpaRepository<PmsCommentReply, Long>, JpaSpecificationExecutor<PmsCommentReply> {
}
