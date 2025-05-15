package com.ad.ssms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.ad.ssms.shop", "com.ad.ssms.attribute", "com.ad.ssms.category", "com.ad.ssms.product", "com.ad.ssms.delivery", "com.ad.ssms.payment", "com.ad.ssms.sale", "com.ad.ssms.purchase"})  // Ensure the repository packages are scanned
@EntityScan(basePackages = {"com.ad.ssms.shop", "com.ad.ssms.attribute", "com.ad.ssms.category", "com.ad.ssms.product", "com.ad.ssms.delivery", "com.ad.ssms.payment", "com.ad.ssms.sale", "com.ad.ssms.purchase"})  // Ensure the entity packages are scanned
public class SsmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsmsApplication.class, args);
    }
}
