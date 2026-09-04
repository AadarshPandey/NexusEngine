package com.nexusengine.core.repository;

import com.nexusengine.core.model.UmsMemberTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UmsMemberTagRepository extends JpaRepository<UmsMemberTag, Long>, JpaSpecificationExecutor<UmsMemberTag> {
}
