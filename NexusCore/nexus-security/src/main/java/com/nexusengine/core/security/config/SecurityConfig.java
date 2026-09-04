package com.nexusengine.core.security.config;

import com.nexusengine.core.security.component.DynamicAuthorizationManager;
import com.nexusengine.core.security.component.JwtAuthenticationTokenFilter;
import com.nexusengine.core.security.component.RestAuthenticationEntryPoint;
import com.nexusengine.core.security.component.RestfulAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * Auto-generated documentation
 * Created by macro on 2019/11/5.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;
    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Autowired(required = false)
    private DynamicAuthorizationManager dynamicAuthorizationManager;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(registry -> {
            // Auto-generated documentation
            for (String url : ignoreUrlsConfig.getUrls()) {
                registry.requestMatchers(url).permitAll();
            }
            // Auto-generated documentation
            registry.requestMatchers(HttpMethod.OPTIONS).permitAll();
            
            // Auto-generated documentation
            registry.anyRequest().access(dynamicAuthorizationManager==null? AuthenticatedAuthorizationManager.authenticated():dynamicAuthorizationManager);
        })
        // Auto-generated documentation
        .csrf(AbstractHttpConfigurer::disable)
        // Auto-generated documentation
        .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // Auto-generated documentation
        .exceptionHandling(configurer -> configurer.accessDeniedHandler(restfulAccessDeniedHandler).authenticationEntryPoint(restAuthenticationEntryPoint))
        // Auto-generated documentation
        .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

}
