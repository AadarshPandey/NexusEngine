package com.nexusengine.core.domain.ordering.repository;
import com.nexusengine.core.domain.ordering.entity.MerchantAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
public interface MerchantAddressRepository extends JpaRepository<MerchantAddress, Long>, JpaSpecificationExecutor<MerchantAddress> {}
