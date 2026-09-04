package com.nexusengine.core.repository;

import com.nexusengine.core.model.CmsHelpCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CmsHelpCategoryRepository extends JpaRepository<CmsHelpCategory, Long>, JpaSpecificationExecutor<CmsHelpCategory> {
}
