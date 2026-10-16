package com.nexusengine.core.domain.promotion.repository;

import com.nexusengine.core.domain.promotion.entity.FlashSale;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlashSaleRepository extends JpaRepository<FlashSale, Long> {
    Page<FlashSale> findByTitleContaining(String title, Pageable pageable);

    List<FlashSale> findByStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Boolean status, LocalDate startDate, LocalDate endDate);
}
