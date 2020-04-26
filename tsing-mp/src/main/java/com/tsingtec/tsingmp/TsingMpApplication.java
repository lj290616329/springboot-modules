package com.tsingtec.tsingmp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication(scanBasePackages="com.tsingtec.**")
public class TsingMpApplication {

    public static void main(String[] args) {
        SpringApplication.run(TsingMpApplication.class, args);
    }

}
