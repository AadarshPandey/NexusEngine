package com.nexusengine.core.repository;

import com.nexusengine.core.model.UmsAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UmsAdminRepository extends JpaRepository<UmsAdmin, Long>, JpaSpecificationExecutor<UmsAdmin> {
    UmsAdmin findByUsername(String username);
}
