package com.digitalchina.resourcecatalog.core;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.digitalchina.resourcecatalog.db", "com.digitalchina.resourcecatalog.core"})
@MapperScan("com.digitalchina.resourcecatalog.db.dao")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}