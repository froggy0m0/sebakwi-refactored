package com.froggy.sebakwi.util.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해
            .allowedOrigins("http://localhost:3000") // 개발환경만 허용
            .allowedMethods("GET", "POST", "PUT") // 허용할 HTTP 메소드
            .allowedHeaders("*") // 모든 헤더 허용
            .allowCredentials(true) // 쿠키를 포함한 요청 허용
            .maxAge(3600); // 3600초 동안 pre-flight 결과 캐시
    }
}
