package com.nexusengine.core.repository;

import com.nexusengine.core.model.CmsTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CmsTopicRepository extends JpaRepository<CmsTopic, Long>, JpaSpecificationExecutor<CmsTopic> {
}
