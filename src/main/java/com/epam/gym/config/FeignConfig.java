package com.epam.gym.config;

import feign.RequestInterceptor;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            String transactionId = MDC.get("transactionId");

            if (transactionId != null) {
                template.header("X-Transaction-Id", transactionId);
                template.request().header("X-Transaction-Id", transactionId);
                System.out.println("Transaction ID in Feign: " + transactionId);
            }
        };
    }
}