package com.schoolmanagementsystem.SchoolManagementSystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {


    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .servers(List.of(
                        new Server()
                                .url("https://w0vhrv2j-8080.inc1.devtunnels.ms/")
                                .description("DevTunnel Server")
                ));
    }


}
