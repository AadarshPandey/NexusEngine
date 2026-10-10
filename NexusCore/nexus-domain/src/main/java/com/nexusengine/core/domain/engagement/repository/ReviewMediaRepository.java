package com.nexusengine.core.domain.engagement.repository;

import com.nexusengine.core.domain.engagement.entity.ReviewMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewMediaRepository extends JpaRepository<ReviewMedia, Long> {
    List<ReviewMedia> findByReviewId(Long reviewId);
}
