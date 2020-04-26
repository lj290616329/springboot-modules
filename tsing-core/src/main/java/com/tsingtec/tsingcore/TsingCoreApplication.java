package com.tsingtec.tsingcore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication(scanBasePackages="com.tsingtec.**")
public class TsingCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(TsingCoreApplication.class, args);
    }

}
