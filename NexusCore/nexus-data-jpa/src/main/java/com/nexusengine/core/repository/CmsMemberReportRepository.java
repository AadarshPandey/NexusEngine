package com.nexusengine.core.repository;

import com.nexusengine.core.model.CmsMemberReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CmsMemberReportRepository extends JpaRepository<CmsMemberReport, Long>, JpaSpecificationExecutor<CmsMemberReport> {
}
