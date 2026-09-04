package com.nexusengine.core.repository;
import com.nexusengine.core.model.CmsSubjectProductRelation;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CmsSubjectProductRelationRepository extends JpaRepository<CmsSubjectProductRelation, Long> {
    void deleteByProductId(Long productId);
}
