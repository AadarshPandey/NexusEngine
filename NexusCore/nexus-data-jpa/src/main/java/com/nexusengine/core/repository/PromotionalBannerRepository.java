package com.nexusengine.core.repository;

import com.nexusengine.core.model.PromotionalBanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PromotionalBannerRepository extends JpaRepository<PromotionalBanner, Long>, JpaSpecificationExecutor<PromotionalBanner> {
    List<PromotionalBanner> findByTypeAndStatusOrderBySortDesc(Integer type, Integer status);
    java.util.List<com.nexusengine.core.model.PromotionalBanner> findByStatusOrderBySortDesc(Integer status);
}
