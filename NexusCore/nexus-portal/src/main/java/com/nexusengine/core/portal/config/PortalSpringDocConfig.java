package com.nexusengine.core.portal.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Auto-generated documentation
 * Created by macro on 2024/3/5.
 */
@Configuration
public class PortalSpringDocConfig implements WebMvcConfigurer {

    private static final String SECURITY_SCHEME_NAME = "Authorization";

    @Bean
    public OpenAPI nexusPortalOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Success")
                        .description("Success")
                        .version("v1.0.0")
                        .license(new License().name("Apache 2.0")
                                .url("https://github.com/macrozheng/nexus-learning")))
                .externalDocs(new ExternalDocumentation()
                        .description("Success")
                        .url("http://www.macrozheng.com"))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Auto-generated documentation
        registry.addViewController("/swagger-ui/").setViewName("redirect:/swagger-ui/index.html");
    }

}

