package com.tpisoftware.org.stlucia.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 禁用 CSRF 保護（使用新版語法）
                .csrf(csrf -> csrf.disable())

                // 設置權限管理
                .authorizeHttpRequests(authorize -> authorize
                        // Swagger 相關路徑設置為可無需登入訪問
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()

                        // 允許所有其他請求無需登入（開發測試用）
                        .anyRequest().permitAll()
                )

                // 不啟用登入表單（完全取消安全性）
                .formLogin(formLogin -> formLogin.disable());

        return http.build();
    }
}
