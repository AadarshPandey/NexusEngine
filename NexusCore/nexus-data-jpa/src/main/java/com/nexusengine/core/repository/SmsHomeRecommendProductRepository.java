package com.nexusengine.core.repository;

import com.nexusengine.core.model.SmsHomeRecommendProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SmsHomeRecommendProductRepository extends JpaRepository<SmsHomeRecommendProduct, Long>, JpaSpecificationExecutor<SmsHomeRecommendProduct> {
}
