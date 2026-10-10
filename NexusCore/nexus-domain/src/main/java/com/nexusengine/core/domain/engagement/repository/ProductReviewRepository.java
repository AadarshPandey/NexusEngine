package com.nexusengine.core.domain.engagement.repository;

import com.nexusengine.core.domain.engagement.entity.ProductReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long>, JpaSpecificationExecutor<ProductReview> {
    
    @EntityGraph(attributePaths = {"attachmentList"})
    Page<ProductReview> findByProductIdAndParentIdIsNullAndStatus(Long productId, Integer status, Pageable pageable);
    
    @EntityGraph(attributePaths = {"attachmentList"})
    List<ProductReview> findByParentIdAndStatus(Long parentId, Integer status);
}
