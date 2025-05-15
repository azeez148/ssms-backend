package com.ad.ssms.config;

import com.ad.ssms.shop.ShopService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShopServiceConfig {

    @Bean
    public ShopService shopService() {
        return new ShopService();
    }

    // Remove the shopRepository bean definition as it will be managed by Spring Data JPA
}