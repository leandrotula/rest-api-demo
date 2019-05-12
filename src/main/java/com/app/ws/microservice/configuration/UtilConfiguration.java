package com.app.ws.microservice.configuration;

import com.app.ws.microservice.ContextAware;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ContextAware contextAware() {
        return new ContextAware();
    }


}
