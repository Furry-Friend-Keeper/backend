package com.example.furryfriendkeeper;

import com.example.furryfriendkeeper.properties.FileStorageProperties;
import com.example.furryfriendkeeper.utils.ListMapper;
import org.modelmapper.ModelMapper;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
@Configuration
public class ApplicationConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    @Bean
    public ListMapper listMapper() {
        return ListMapper.getInstance();
    }

}