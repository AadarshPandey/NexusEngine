package com.nexusengine.core.domain.catalog.repository;
import com.nexusengine.core.domain.catalog.entity.ProductApprovalEvent;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProductApprovalEventRepository extends JpaRepository<ProductApprovalEvent, Long> {
    java.util.List<ProductApprovalEvent> findByProductId(Long productId);
}
