package com.nexusengine.core.domain.iam.repository;

import com.nexusengine.core.domain.iam.entity.ConsoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConsoleMenuRepository extends JpaRepository<ConsoleMenu, Long>, JpaSpecificationExecutor<ConsoleMenu> {
    Page<ConsoleMenu> findByParentId(Long parentId, Pageable pageable);
}
