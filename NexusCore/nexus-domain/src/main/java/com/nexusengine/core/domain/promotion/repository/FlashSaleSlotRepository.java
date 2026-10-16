package com.nexusengine.core.domain.promotion.repository;

import com.nexusengine.core.domain.promotion.entity.FlashSaleSlot;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FlashSaleSlotRepository extends JpaRepository<FlashSaleSlot, Long> {
    List<FlashSaleSlot> findByStatus(Boolean status);

    Optional<FlashSaleSlot> findFirstByStatusAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            Boolean status, LocalTime startTime, LocalTime endTime);
}
