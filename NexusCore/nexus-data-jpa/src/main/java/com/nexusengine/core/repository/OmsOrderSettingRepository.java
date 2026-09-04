package com.nexusengine.core.repository;

import com.nexusengine.core.model.OmsOrderSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OmsOrderSettingRepository extends JpaRepository<OmsOrderSetting, Long>, JpaSpecificationExecutor<OmsOrderSetting> {
}
