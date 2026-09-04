package com.nexusengine.core.repository;
import com.nexusengine.core.model.PmsProductLadder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PmsProductLadderRepository extends JpaRepository<PmsProductLadder, Long> {
    List<PmsProductLadder> findByProductId(Long productId);
    void deleteByProductId(Long productId);
}
