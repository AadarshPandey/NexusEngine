package com.nexusengine.core.repository;

import com.nexusengine.core.model.PmsProductAttributeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

public interface PmsProductAttributeCategoryRepository extends JpaRepository<PmsProductAttributeCategory, Long>, JpaSpecificationExecutor<PmsProductAttributeCategory> {
}
