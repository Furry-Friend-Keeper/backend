package com.example.furryfriendkeeper;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfigurer implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
//                .allowedOrigins("http://cp23at3.sit.kmutt.ac.th")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "HEAD", "PUT", "DELETE","PATCH");
    }
}