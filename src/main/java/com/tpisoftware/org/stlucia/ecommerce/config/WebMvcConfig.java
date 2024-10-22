package com.tpisoftware.org.stlucia.ecommerce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 使用 WebMvcConfigurerAdapter 擴展 SpringMVC 的功能
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // 配置靜態資源路徑
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    // 添加一到多個視圖映射
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 瀏覽器發送 / 請求後，來到 login 頁面。
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/index.html").setViewName("login");
    }

}