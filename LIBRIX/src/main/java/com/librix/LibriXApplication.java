package com.librix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LibriXApplication {
    public static void main(String[] args) {
        SpringApplication.run(LibriXApplication.class, args);
    }
}
