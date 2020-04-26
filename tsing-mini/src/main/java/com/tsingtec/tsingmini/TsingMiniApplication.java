package com.tsingtec.tsingmini;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication(scanBasePackages="com.tsingtec.**")
public class TsingMiniApplication {

    public static void main(String[] args) {
        SpringApplication.run(TsingMiniApplication.class, args);
    }

}
