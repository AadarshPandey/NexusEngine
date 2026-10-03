package com.nexusengine.core.repository;

import com.nexusengine.core.model.PmsReviewAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PmsReviewAttachmentRepository extends JpaRepository<PmsReviewAttachment, Long> {
    List<PmsReviewAttachment> findByReviewId(Long reviewId);
}
