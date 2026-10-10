package com.nexusengine.core.domain.iam.repository;

import com.nexusengine.core.domain.iam.entity.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long>, JpaSpecificationExecutor<AdminUser> {
    AdminUser findByUsername(String username);
}
