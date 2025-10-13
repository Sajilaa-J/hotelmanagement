//package com.booking_service.scheduler;
//
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.core.task.TaskExecutor;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//@Configuration
//public class AsyncConfig {
//
//    @Bean
//    @Lazy
//    public TaskExecutor applicationTaskExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(5);
//        executor.setMaxPoolSize(10);
//        executor.setQueueCapacity(25);
//        executor.setThreadNamePrefix("AppExecutor-");
//        executor.initialize();
//        return executor;
//    }
//}
