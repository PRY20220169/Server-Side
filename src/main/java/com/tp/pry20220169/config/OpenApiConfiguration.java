package com.tp.pry20220169.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
    @Bean(name = "PRY20220169 OpenApi")
    public OpenAPI PRY20220169OpenApi(){
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("PRY20220169 Application API")
                        .description(
                                "PRY20220169 API implemented with Spring Boot RESTful service and documented using springdoc-openapi and OpenAPI3."));
    }
}
