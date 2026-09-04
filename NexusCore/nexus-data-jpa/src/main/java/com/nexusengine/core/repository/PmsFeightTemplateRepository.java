package com.nexusengine.core.repository;

import com.nexusengine.core.model.PmsFeightTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PmsFeightTemplateRepository extends JpaRepository<PmsFeightTemplate, Long>, JpaSpecificationExecutor<PmsFeightTemplate> {
}
