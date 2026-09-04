package com.nexusengine.core.repository;
import com.nexusengine.core.model.OmsCompanyAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
public interface OmsCompanyAddressRepository extends JpaRepository<OmsCompanyAddress, Long>, JpaSpecificationExecutor<OmsCompanyAddress> {}
