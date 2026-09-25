package com.nexusengine.core.repository;
import com.nexusengine.core.model.PmsProductVerifyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PmsProductVerifyRecordRepository extends JpaRepository<PmsProductVerifyRecord, Long> {
    java.util.List<PmsProductVerifyRecord> findByProductId(Long productId);
}
