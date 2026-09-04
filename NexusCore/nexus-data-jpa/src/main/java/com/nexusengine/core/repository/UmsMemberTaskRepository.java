package com.nexusengine.core.repository;

import com.nexusengine.core.model.UmsMemberTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UmsMemberTaskRepository extends JpaRepository<UmsMemberTask, Long>, JpaSpecificationExecutor<UmsMemberTask> {
}
