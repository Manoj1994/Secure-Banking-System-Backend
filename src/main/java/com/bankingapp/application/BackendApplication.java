package com.bankingapp.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan("com.bankingapp")
@EnableJpaRepositories("com.bankingapp.repository")
@EntityScan("com.bankingapp.model")
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class BackendApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(BackendApplication.class, args);
//        ThreadForDeletingRow thread = (ThreadForDeletingRow) applicationContext.getBean("threadForDeletingRow");
//        thread.setName("Clean bad logins");
//        thread.start();
    }
}
