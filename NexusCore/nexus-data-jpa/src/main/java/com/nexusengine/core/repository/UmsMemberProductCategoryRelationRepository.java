package com.nexusengine.core.repository;

import com.nexusengine.core.model.UmsMemberProductCategoryRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UmsMemberProductCategoryRelationRepository extends JpaRepository<UmsMemberProductCategoryRelation, Long>, JpaSpecificationExecutor<UmsMemberProductCategoryRelation> {
}
