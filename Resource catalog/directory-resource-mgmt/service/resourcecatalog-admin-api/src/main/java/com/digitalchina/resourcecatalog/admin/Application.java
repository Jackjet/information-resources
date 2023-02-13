package com.digitalchina.resourcecatalog.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.digitalchina.resourcecatalog.db", "com.digitalchina.resourcecatalog.core", "com" +
        ".digitalchina.resourcecatalog.admin","d1"})
@MapperScan("com.digitalchina.resourcecatalog.db.dao")
@EnableTransactionManagement
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}