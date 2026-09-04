package com.nexusengine.core.repository;

import com.nexusengine.core.model.CmsSubjectComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CmsSubjectCommentRepository extends JpaRepository<CmsSubjectComment, Long>, JpaSpecificationExecutor<CmsSubjectComment> {
}
