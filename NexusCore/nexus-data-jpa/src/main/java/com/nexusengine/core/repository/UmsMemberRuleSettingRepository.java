package com.nexusengine.core.repository;

import com.nexusengine.core.model.UmsMemberRuleSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UmsMemberRuleSettingRepository extends JpaRepository<UmsMemberRuleSetting, Long>, JpaSpecificationExecutor<UmsMemberRuleSetting> {
}
