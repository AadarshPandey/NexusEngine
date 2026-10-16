package com.nexusengine.core.config;

import com.nexusengine.core.admin.iam.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Admin IAM UserDetailsService configuration.
 */
@Configuration
@RequiredArgsConstructor
public class AdminNexusSecurityConfig {

    private final AdminUserService adminService;

    @Bean
    public UserDetailsService adminUserDetailsService() {
        return adminService::loadUserByUsername;
    }
}
