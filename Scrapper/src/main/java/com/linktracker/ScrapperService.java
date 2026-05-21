package com.linktracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ScrapperService {
    public static void main(String[] args) {
        SpringApplication.run(ScrapperService.class, args);
    }
}