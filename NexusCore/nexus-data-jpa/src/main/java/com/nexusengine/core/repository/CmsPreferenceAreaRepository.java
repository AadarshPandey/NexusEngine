package com.nexusengine.core.repository;
import com.nexusengine.core.model.CmsPreferenceArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
public interface CmsPreferenceAreaRepository extends JpaRepository<CmsPreferenceArea, Long>, JpaSpecificationExecutor<CmsPreferenceArea> {}
