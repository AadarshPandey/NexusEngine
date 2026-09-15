package com.nexusengine.core.repository;

import com.nexusengine.core.model.PmsReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

public interface PmsReviewRepository extends JpaRepository<PmsReview, Long>, JpaSpecificationExecutor<PmsReview> {
    
    @EntityGraph(attributePaths = {"mediaList"})
    Page<PmsReview> findByProductIdAndParentIdIsNullAndStatus(Long productId, Integer status, Pageable pageable);
    
    @EntityGraph(attributePaths = {"mediaList"})
    List<PmsReview> findByParentIdAndStatus(Long parentId, Integer status);
}
