package com.jerry.jtakeaway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class JtakeawayApplication {

    public static void main(String[] args) {
        SpringApplication.run(JtakeawayApplication.class, args);
    }

}
