package com.nexusengine.core.domain.customer.repository;

import com.nexusengine.core.domain.customer.entity.MemberAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MemberAddressRepository extends JpaRepository<MemberAddress, Long>, JpaSpecificationExecutor<MemberAddress> {
    List<MemberAddress> findByMemberId(Long memberId);
}
