package com.nexusengine.core.repository;

import com.nexusengine.core.model.UmsMemberReceiveAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface UmsMemberReceiveAddressRepository extends JpaRepository<UmsMemberReceiveAddress, Long>, JpaSpecificationExecutor<UmsMemberReceiveAddress> {
    List<UmsMemberReceiveAddress> findByMemberId(Long memberId);
}
