package com.tang.project.config.xss;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XssJsonConfig {

    /**
     * 注册自定义的Jackson反序列器
     *
     * @return Module
     */
    @Bean
    public Module xssModule() {
        SimpleModule module = new SimpleModule();
        // 处理提交数据的序列化
        module.addDeserializer(String.class, new XssJsonDeserializer());
        // 处理返回数据的序列化（旧数据的处理）
        module.addSerializer(String.class, new XssJsonSerializer());
        return module;


    }

}
