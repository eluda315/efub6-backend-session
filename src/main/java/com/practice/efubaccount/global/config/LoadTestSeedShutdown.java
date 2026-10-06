package com.practice.efubaccount.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

// Seeder의 트랜잭션 완료 후 seed 전용 Spring 프로세스를 종료한다.
// @Component
// @Order(2)
// @RequiredArgsConstructor
// @ConditionalOnProperty(name = "loadtest.seed.exit-after-completion", havingValue = "true")
// public class LoadTestSeedShutdown implements ApplicationRunner {

//     private final ConfigurableApplicationContext applicationContext;

//     @Override
//     public void run(ApplicationArguments args) {
//         SpringApplication.exit(applicationContext);
//     }
// }
