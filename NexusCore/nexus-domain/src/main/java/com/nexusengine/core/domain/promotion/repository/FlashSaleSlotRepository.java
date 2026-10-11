package com.nexusengine.core.domain.promotion.repository;

import com.nexusengine.core.domain.promotion.entity.FlashSaleSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface FlashSaleSlotRepository extends JpaRepository<FlashSaleSlot, Long> {
    List<FlashSaleSlot> findByStatus(Integer status);

    Optional<FlashSaleSlot> findFirstByStatusAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(Integer status, LocalTime startTime, LocalTime endTime);
}
