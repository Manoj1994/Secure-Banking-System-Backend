package com.bankingapp.application;

import com.bankingapp.model.login.SessionManager;
import com.bankingapp.model.login.ThreadForDeletingRow;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Timer;

@ComponentScan("com.bankingapp")
@EnableJpaRepositories("com.bankingapp.repository")
@EntityScan("com.bankingapp.model")
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class BackendApplication {

    public static void main(String[] args) {

        ApplicationContext applicationContext = SpringApplication.run(BackendApplication.class, args);
    }
}
