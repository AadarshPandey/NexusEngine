package com.nexusengine.core.config;

import com.nexusengine.core.domain.iam.entity.Permission;

import com.nexusengine.core.domain.catalog.entity.*;
import com.nexusengine.core.domain.ordering.entity.*;
import com.nexusengine.core.domain.customer.entity.*;
import com.nexusengine.core.domain.promotion.entity.*;
import com.nexusengine.core.domain.iam.entity.*;
import com.nexusengine.core.domain.catalog.repository.*;
import com.nexusengine.core.domain.ordering.repository.*;
import com.nexusengine.core.domain.customer.repository.*;
import com.nexusengine.core.domain.promotion.repository.*;
import com.nexusengine.core.domain.iam.repository.*;
import com.nexusengine.core.admin.catalog.dto.*;
import com.nexusengine.core.admin.catalog.service.*;
import com.nexusengine.core.admin.analytics.service.*;
import com.nexusengine.core.admin.ordering.dto.*;
import com.nexusengine.core.admin.ordering.service.*;
import com.nexusengine.core.admin.promotion.dto.*;
import com.nexusengine.core.admin.promotion.service.*;
import com.nexusengine.core.admin.ai.service.*;
import com.nexusengine.core.admin.infra.dto.*;
import com.nexusengine.core.admin.infra.service.*;
import com.nexusengine.core.admin.iam.dto.*;
import com.nexusengine.core.admin.iam.service.*;
import com.nexusengine.core.security.component.DynamicSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Represents the AdminNexusSecurityConfig component.
 * Provides core functionality and operations for AdminNexusSecurityConfig.
 */
@Configuration
public class AdminNexusSecurityConfig {

    private final AdminUserService adminService;
    private final PermissionService resourceService;

    public AdminNexusSecurityConfig(AdminUserService adminService, PermissionService resourceService) {
        this.adminService = adminService;
        this.resourceService = resourceService;
    }

    @Bean
    public UserDetailsService adminUserDetailsService() {
        return username -> adminService.loadUserByUsername(username);
    }

    @Bean
    public DynamicSecurityService dynamicSecurityService() {
        return new DynamicSecurityService() {
            @Override
            public Map<String, String> loadDataSource() {
                Map<String, String> map = new ConcurrentHashMap<>();
                List<Permission> resourceList = resourceService.listAll();
                for (Permission resource : resourceList) {
                    map.put(resource.getUrl(), resource.getId() + ":" + resource.getName());
                }
                return map;
            }
        };
    }
}
