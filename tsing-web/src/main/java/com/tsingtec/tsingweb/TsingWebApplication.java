package com.tsingtec.tsingweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@EnableCaching
@SpringBootApplication(scanBasePackages="com.tsingtec.**")
public class TsingWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(TsingWebApplication.class, args);
    }

}
