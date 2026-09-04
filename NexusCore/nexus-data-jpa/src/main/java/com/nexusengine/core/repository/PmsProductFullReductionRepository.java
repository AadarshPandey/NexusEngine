package com.nexusengine.core.repository;
import com.nexusengine.core.model.PmsProductFullReduction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PmsProductFullReductionRepository extends JpaRepository<PmsProductFullReduction, Long> {
    List<PmsProductFullReduction> findByProductId(Long productId);
    void deleteByProductId(Long productId);
}
