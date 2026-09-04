package com.nexusengine.core.repository;

import com.nexusengine.core.model.CmsSubjectCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CmsSubjectCategoryRepository extends JpaRepository<CmsSubjectCategory, Long>, JpaSpecificationExecutor<CmsSubjectCategory> {
}
