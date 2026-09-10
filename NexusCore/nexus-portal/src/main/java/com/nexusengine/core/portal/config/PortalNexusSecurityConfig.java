package com.nexusengine.core.portal.config;

import com.nexusengine.core.portal.service.UmsMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Represents the PortalNexusSecurityConfig component.
 * Provides core functionality and operations for PortalNexusSecurityConfig.
 */
@Configuration
public class PortalNexusSecurityConfig {

    @Autowired
    private UmsMemberService memberService;

    @Bean
    public UserDetailsService memberUserDetailsService() {
        return username -> memberService.loadUserByUsername(username);
    }
}
