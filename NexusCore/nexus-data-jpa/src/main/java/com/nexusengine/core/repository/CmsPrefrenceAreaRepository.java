package com.nexusengine.core.repository;
import com.nexusengine.core.model.CmsPrefrenceArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
public interface CmsPrefrenceAreaRepository extends JpaRepository<CmsPrefrenceArea, Long>, JpaSpecificationExecutor<CmsPrefrenceArea> {}
