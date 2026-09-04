package com.nexusengine.core.repository;

import com.nexusengine.core.model.SmsHomeAdvertise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SmsHomeAdvertiseRepository extends JpaRepository<SmsHomeAdvertise, Long>, JpaSpecificationExecutor<SmsHomeAdvertise> {
    List<SmsHomeAdvertise> findByTypeAndStatusOrderBySortDesc(Integer type, Integer status);
}
