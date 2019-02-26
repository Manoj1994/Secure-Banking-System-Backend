package com.bankingapp.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.bankingapp")
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class BackendApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(BackendApplication.class, args);
    }
}
