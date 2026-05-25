package com.banking.accounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AccountsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountsServiceApplication.class, args);
    }
}
