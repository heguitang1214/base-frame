package com.tang.project.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonEnumDeserializerConfig {


    @Bean
    public Module enumModule() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Enum.class, new EnumDeserializer());
        return module;
    }

}
