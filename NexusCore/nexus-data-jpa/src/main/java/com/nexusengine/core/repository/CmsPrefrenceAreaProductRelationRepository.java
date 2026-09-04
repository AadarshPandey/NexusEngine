package com.nexusengine.core.repository;
import com.nexusengine.core.model.CmsPrefrenceAreaProductRelation;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CmsPrefrenceAreaProductRelationRepository extends JpaRepository<CmsPrefrenceAreaProductRelation, Long> {
    void deleteByProductId(Long productId);
}
