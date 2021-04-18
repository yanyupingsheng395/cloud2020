package com.atguigu.springcloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationContextConfig {
    @Bean
    //@LoadBalanced//Ribbon的轮询策略
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

}
