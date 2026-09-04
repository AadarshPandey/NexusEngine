package com.nexusengine.core.repository;

import com.nexusengine.core.model.PmsProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PmsProductRepository extends JpaRepository<PmsProduct, Long>, JpaSpecificationExecutor<PmsProduct> {
    List<PmsProduct> findByVendorId(Long vendorId);
    List<PmsProduct> findByNameContaining(String name);
    List<PmsProduct> findByVendorIdAndNameContaining(Long vendorId, String name);
    Page<PmsProduct> findByBrandIdAndDeleteStatusAndPublishStatus(Long brandId, Integer deleteStatus, Integer publishStatus, Pageable pageable);
}
