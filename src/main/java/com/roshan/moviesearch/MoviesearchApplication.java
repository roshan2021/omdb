package com.roshan.moviesearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class MoviesearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoviesearchApplication.class, args);
    }

}
