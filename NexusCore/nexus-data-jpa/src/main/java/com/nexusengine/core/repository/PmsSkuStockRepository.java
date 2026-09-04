package com.nexusengine.core.repository;
import com.nexusengine.core.model.PmsSkuStock;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PmsSkuStockRepository extends JpaRepository<PmsSkuStock, Long> {
    List<PmsSkuStock> findByProductId(Long productId);
    List<PmsSkuStock> findByProductIdAndSkuCodeContaining(Long productId, String skuCode);
    void deleteByProductId(Long productId);
}
