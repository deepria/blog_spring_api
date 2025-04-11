package com.blog.spring_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Package Name : com.blog.spring_api.config
 * File Name : WebConfig
 * Description :
 * author ejlee@goodsoft.io
 *
 * @version 1.0
 * @see com.blog.spring_api.config
 * @since 2024-12-26
 * <p>
 * Modification Information
 * 수정일          수정자                    수정내용
 * --------- ------------------- -------------------------------
 * 2024-12-26        eojin                   최초개정
 */
@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 모든 경로에 대해 허용
                        .allowedOrigins(
                                "https://lhmzq4cmgd.execute-api.ap-northeast-2.amazonaws.com/stage" // API Gateway 도메인
                                , "https://main.d39hqh4ds9p1ue.amplifyapp.com" // Amplify 도메인
                                , "https://blog-api-load-balancer-992071478.ap-northeast-2.elb.amazonaws.com/helloWorld"
                                , "http://localhost:5173/" // 로컬 vue 도메인
                                , "http://127.0.0.1:5173/" // 로컬 vue 도메인
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
                        .allowedHeaders("*") // 모든 헤더 허용
                        .allowCredentials(true); // 인증 정보 허용
            }
        };
    }
}
