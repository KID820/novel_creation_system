package com.gdut.aigc;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:7000","http://localhost:7001")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 访问 /images/xxx.jpg 实际读取 D:/JavaWeb/gdut/homework/image/xxx.jpg
       /* registry.addResourceHandler("/images/**")
                .addResourceLocations("file:D:/JavaWeb/gdut/homework/image/");*/
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:/java/image/");
    }


}
