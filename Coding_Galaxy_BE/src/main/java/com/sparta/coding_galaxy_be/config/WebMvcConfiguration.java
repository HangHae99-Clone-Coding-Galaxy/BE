package com.sparta.coding_galaxy_be.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry){
        corsRegistry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "http://43.201.75.53:8080")
                .allowedMethods("*")
                .exposedHeaders("Authorization", "RefreshToken")
                .allowCredentials(true);
    }
}
