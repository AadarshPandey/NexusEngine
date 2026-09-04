package com.nexusengine.core.repository;

import com.nexusengine.core.model.UmsResourceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UmsResourceCategoryRepository extends JpaRepository<UmsResourceCategory, Long>, JpaSpecificationExecutor<UmsResourceCategory> {
}
