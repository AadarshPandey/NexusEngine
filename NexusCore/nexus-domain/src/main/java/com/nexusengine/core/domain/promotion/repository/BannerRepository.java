package com.nexusengine.core.domain.promotion.repository;

import com.nexusengine.core.domain.promotion.entity.Banner;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BannerRepository extends JpaRepository<Banner, Long>, JpaSpecificationExecutor<Banner> {
    List<Banner> findByTypeAndStatusOrderByDisplayOrderDesc(Integer type, Boolean status);

    List<Banner> findByStatusOrderByDisplayOrderDesc(Boolean status);
}
