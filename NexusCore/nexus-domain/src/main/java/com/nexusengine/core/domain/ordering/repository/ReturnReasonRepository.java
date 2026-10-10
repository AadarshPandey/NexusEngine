package com.nexusengine.core.domain.ordering.repository;
import com.nexusengine.core.domain.ordering.entity.ReturnReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
public interface ReturnReasonRepository extends JpaRepository<ReturnReason, Long>, JpaSpecificationExecutor<ReturnReason> {}
