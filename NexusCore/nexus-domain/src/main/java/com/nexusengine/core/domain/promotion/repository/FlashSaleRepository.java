package com.nexusengine.core.domain.promotion.repository;

import com.nexusengine.core.domain.promotion.entity.FlashSale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlashSaleRepository extends JpaRepository<FlashSale, Long> {
    org.springframework.data.domain.Page<FlashSale> findByTitleContaining(String title, org.springframework.data.domain.Pageable pageable);
    List<FlashSale> findByStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Integer status, java.time.LocalDate startDate, java.time.LocalDate endDate);
}
