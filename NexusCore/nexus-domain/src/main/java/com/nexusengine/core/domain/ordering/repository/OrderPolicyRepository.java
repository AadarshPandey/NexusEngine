package com.nexusengine.core.domain.ordering.repository;

import com.nexusengine.core.domain.ordering.entity.OrderPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderPolicyRepository extends JpaRepository<OrderPolicy, Long>, JpaSpecificationExecutor<OrderPolicy> {
}
