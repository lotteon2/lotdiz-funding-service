package com.lotdiz.fundingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class FundingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FundingServiceApplication.class, args);
    }

}
