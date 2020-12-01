package com.tang.project;

import com.tang.project.version.APIVersionHandlerMapping;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 项目基础配置
 * <p>
 * 实现接口：WebMvcRegistrations，完成自定义接口版本的替换
 *
 * @author tang
 */
@SpringBootApplication
public class BaseProjectApplication implements WebMvcRegistrations {

    public static void main(String[] args) {
        SpringApplication.run(BaseProjectApplication.class, args);
    }

    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new APIVersionHandlerMapping();
    }

}
