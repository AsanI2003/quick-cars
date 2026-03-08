package com.project.quickcars2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/home/**")
                        .allowedOrigins("http://localhost:8080")
                        .allowedMethods("GET")
                        .allowedHeaders("X-API-KEY");

                registry.addMapping("/rental/**")
                        .allowedOrigins("http://localhost:8080")
                        .allowedMethods("GET", "POST")
                        .allowedHeaders("X-API-KEY", "Content-Type");

                registry.addMapping("/vehicle/**")
                        .allowedOrigins("http://localhost:8080")
                        .allowedMethods("GET")
                        .allowedHeaders("X-API-KEY");


            }
        };
    }
}
