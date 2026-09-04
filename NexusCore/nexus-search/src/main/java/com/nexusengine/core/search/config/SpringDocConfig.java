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
 * Auto-generated documentation
 * Created by macro on 2024/3/5.
 */
@Configuration
public class SpringDocConfig implements WebMvcConfigurer {

    @Bean
    public OpenAPI mallAdminOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Success")
                        .description("Success")
                        .version("v1.0.0")
                        .license(new License().name("Apache 2.0")
                                .url("https://github.com/macrozheng/mall-learning")))
                .externalDocs(new ExternalDocumentation()
                        .description("Success")
                        .url("http://www.macrozheng.com"));
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Auto-generated documentation
        registry.addViewController("/swagger-ui/").setViewName("redirect:/swagger-ui/index.html");
    }

}

