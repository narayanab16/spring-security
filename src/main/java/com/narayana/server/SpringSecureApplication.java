package com.narayana.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class, SecurityAutoConfiguration.class})
public class SpringSecureApplication {
    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "security");
        SpringApplication.run(SpringSecureApplication.class, args);
    }
}