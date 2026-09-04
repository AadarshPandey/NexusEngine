package com.nexusengine.core.repository;

import com.nexusengine.core.model.SmsHomeNewProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SmsHomeNewProductRepository extends JpaRepository<SmsHomeNewProduct, Long>, JpaSpecificationExecutor<SmsHomeNewProduct> {
}
