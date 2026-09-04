package com.nexusengine.core.repository;

import com.nexusengine.core.model.UmsResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UmsResourceRepository extends JpaRepository<UmsResource, Long>, JpaSpecificationExecutor<UmsResource> {
}
