package com.nexusengine.core.security.config;

import com.nexusengine.core.security.component.*;
import com.nexusengine.core.security.util.JwtTokenUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Represents the CommonSecurityConfig component.
 * Provides core functionality and operations for CommonSecurityConfig.
 */
@Configuration
@EnableConfigurationProperties(IgnoreUrlsConfig.class)
public class CommonSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public JwtTokenUtil jwtTokenUtil() {
        return new JwtTokenUtil();
    }

    @Bean
    public RestfulAccessDeniedHandler restfulAccessDeniedHandler() {
        return new RestfulAccessDeniedHandler();
    }

    @Bean
    public RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(org.springframework.security.core.userdetails.UserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil){
        return new JwtAuthenticationTokenFilter(userDetailsService, jwtTokenUtil);
    }

    @ConditionalOnBean(name = "dynamicSecurityService")
    @Bean
    public DynamicSecurityMetadataSource dynamicSecurityMetadataSource(DynamicSecurityService dynamicSecurityService) {
        return new DynamicSecurityMetadataSource(dynamicSecurityService);
    }

    @ConditionalOnBean(name = "dynamicSecurityService")
    @Bean
    public DynamicAuthorizationManager dynamicAuthorizationManager(DynamicSecurityMetadataSource dynamicSecurityMetadataSource, IgnoreUrlsConfig ignoreUrlsConfig) {
        return new DynamicAuthorizationManager(dynamicSecurityMetadataSource, ignoreUrlsConfig);
    }
}
