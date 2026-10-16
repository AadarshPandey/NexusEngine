package com.nexusengine.core.application.config;

import com.nexusengine.core.admin.iam.service.AdminUserService;
import com.nexusengine.core.portal.customer.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class UnifiedSecurityConfig {

    private final AdminUserService adminService;
    private final MemberService memberService;

    public UnifiedSecurityConfig(AdminUserService adminService, MemberService memberService) {
        this.adminService = adminService;
        this.memberService = memberService;
    }

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        return username -> {
            try {
                UserDetails admin = adminService.loadUserByUsername(username);
                if (admin != null) return admin;
            } catch (UsernameNotFoundException ignored) {
            }
            try {
                UserDetails member = memberService.loadUserByUsername(username);
                if (member != null) return member;
            } catch (UsernameNotFoundException ignored) {
            }
            throw new UsernameNotFoundException("User not found: " + username);
        };
    }
}
