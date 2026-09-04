package com.nexusengine.core.portal.config;

import com.nexusengine.core.portal.service.UmsMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Auto-generated documentation
 * Created by macro on 2019/11/5.
 */
@Configuration
public class PortalNexusSecurityConfig {

    @Autowired
    private UmsMemberService memberService;

    @Bean
    public UserDetailsService memberUserDetailsService() {
        // Auto-generated documentation
        return username -> memberService.loadUserByUsername(username);
    }
}
