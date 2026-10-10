package com.nexusengine.core.domain.customer.repository;

import com.nexusengine.core.domain.customer.entity.MemberLoginEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MemberLoginEventRepository extends JpaRepository<MemberLoginEvent, Long>, JpaSpecificationExecutor<MemberLoginEvent> {
}
