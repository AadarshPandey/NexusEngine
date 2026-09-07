package com.nexusengine.core.repository;

import com.nexusengine.core.model.PmsReviewMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PmsReviewMediaRepository extends JpaRepository<PmsReviewMedia, Long> {
    List<PmsReviewMedia> findByReviewId(Long reviewId);
}
