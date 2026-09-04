package com.nexusengine.core.repository;

import com.nexusengine.core.model.UmsMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UmsMenuRepository extends JpaRepository<UmsMenu, Long>, JpaSpecificationExecutor<UmsMenu> {
}
