package com.nexusengine.core.domain.promotion.repository;

import com.nexusengine.core.domain.promotion.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long>, JpaSpecificationExecutor<Banner> {
    List<Banner> findByTypeAndStatusOrderBySortDesc(Integer type, Integer status);
    java.util.List<com.nexusengine.core.domain.promotion.entity.Banner> findByStatusOrderBySortDesc(Integer status);
}
