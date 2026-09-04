package com.nexusengine.core.repository;

import com.nexusengine.core.model.CmsHelp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CmsHelpRepository extends JpaRepository<CmsHelp, Long>, JpaSpecificationExecutor<CmsHelp> {
}
