package com.nexusengine.core.search.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Represents the SpringDocConfig component.
 * Provides core functionality and operations for SpringDocConfig.
 */
@Configuration
public class SpringDocConfig implements WebMvcConfigurer {

    @Bean
    public OpenAPI nexusSearchOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("NexusEngine Search API")
                        .description("Product search and indexing REST API")
                        .version("v1.0.0")
                        .license(new License().name("Apache 2.0")
                                .url("https://github.com/AadarshPandey/NexusEngine")))
                .externalDocs(new ExternalDocumentation()
                        .description("Success")
                        .url("https://github.com/AadarshPandey/NexusEngine"));
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/swagger-ui/").setViewName("redirect:/swagger-ui/index.html");
    }

}

