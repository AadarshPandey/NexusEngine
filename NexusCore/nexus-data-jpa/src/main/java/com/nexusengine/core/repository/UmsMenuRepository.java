package com.nexusengine.core.repository;

import com.nexusengine.core.model.UmsMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UmsMenuRepository extends JpaRepository<UmsMenu, Long>, JpaSpecificationExecutor<UmsMenu> {
    Page<UmsMenu> findByParentId(Long parentId, Pageable pageable);
}
