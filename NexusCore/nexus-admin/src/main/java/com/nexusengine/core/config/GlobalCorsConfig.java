package com.nexusengine.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Auto-generated documentation
 * Created by macro on 2019/7/27.
 */
@Configuration
public class GlobalCorsConfig {

    /**
     * Auto-generated documentation
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // Auto-generated documentation
        config.addAllowedOriginPattern("*");
        // Auto-generated documentation
        config.setAllowCredentials(true);
        // Auto-generated documentation
        config.addAllowedHeader("*");
        // Auto-generated documentation
        config.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
