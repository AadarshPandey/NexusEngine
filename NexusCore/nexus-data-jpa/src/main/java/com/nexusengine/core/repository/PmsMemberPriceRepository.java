package com.nexusengine.core.repository;
import com.nexusengine.core.model.PmsMemberPrice;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PmsMemberPriceRepository extends JpaRepository<PmsMemberPrice, Long> {
    void deleteByProductId(Long productId);
}
