package com.nexusengine.core.domain.customer.repository;

import com.nexusengine.core.domain.customer.entity.MemberCategoryInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MemberCategoryInterestRepository extends JpaRepository<MemberCategoryInterest, Long>, JpaSpecificationExecutor<MemberCategoryInterest> {
}
