package com.forensicdna;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.forensicdna")
public class ForensicDnaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForensicDnaApplication.class, args);
    }
}