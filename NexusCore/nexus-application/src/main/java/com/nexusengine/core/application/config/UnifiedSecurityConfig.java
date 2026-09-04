package com.nexusengine.core.application.config;

import com.nexusengine.core.service.UmsAdminService;
import com.nexusengine.core.portal.service.UmsMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class UnifiedSecurityConfig {

    @Autowired
    private UmsAdminService adminService;

    @Autowired
    private UmsMemberService memberService;

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        return username -> {
            try {
                UserDetails admin = adminService.loadUserByUsername(username);
                if (admin != null) return admin;
            } catch (UsernameNotFoundException e) {
                // Ignore and try member
            }
            try {
                UserDetails member = memberService.loadUserByUsername(username);
                if (member != null) return member;
            } catch (UsernameNotFoundException e) {
                throw new UsernameNotFoundException("User not found in admin or member tables.");
            }
            throw new UsernameNotFoundException("User not found");
        };
    }
}
