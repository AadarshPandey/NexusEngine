package com.nexusengine.core.repository;

import com.nexusengine.core.model.UmsMemberLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface UmsMemberLevelRepository extends JpaRepository<UmsMemberLevel, Long>, JpaSpecificationExecutor<UmsMemberLevel> {
    List<UmsMemberLevel> findByDefaultStatus(Integer defaultStatus);
}
