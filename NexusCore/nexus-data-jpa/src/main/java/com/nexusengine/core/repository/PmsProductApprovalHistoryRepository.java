package com.nexusengine.core.repository;
import com.nexusengine.core.model.PmsProductApprovalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PmsProductApprovalHistoryRepository extends JpaRepository<PmsProductApprovalHistory, Long> {
    java.util.List<PmsProductApprovalHistory> findByProductId(Long productId);
}
