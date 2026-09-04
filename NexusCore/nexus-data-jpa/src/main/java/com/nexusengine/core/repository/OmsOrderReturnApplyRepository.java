package com.nexusengine.core.repository;

import com.nexusengine.core.model.OmsOrderReturnApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OmsOrderReturnApplyRepository extends JpaRepository<OmsOrderReturnApply, Long>, JpaSpecificationExecutor<OmsOrderReturnApply> {
}
