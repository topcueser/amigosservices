package com.topcueser.customer;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CustomerConfig {

    @Bean
    @LoadBalanced   // http://FRAUD olarak application name ile tanınması ve birden fazla app için çalışabilmesi
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
