package com.lotdiz.fundingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableEurekaClient
@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
public class FundingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FundingServiceApplication.class, args);
    }

}
