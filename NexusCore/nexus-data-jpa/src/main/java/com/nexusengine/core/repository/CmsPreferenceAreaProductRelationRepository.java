package com.nexusengine.core.repository;
import com.nexusengine.core.model.CmsPreferenceAreaProductRelation;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CmsPreferenceAreaProductRelationRepository extends JpaRepository<CmsPreferenceAreaProductRelation, Long> {
    void deleteByProductId(Long productId);
}
