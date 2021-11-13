package org.tms.task_executor_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = "org.tms.task_executor_service.persistent")
@SpringBootApplication
public class TaskExecutorServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskExecutorServiceApplication.class, args);
    }

}