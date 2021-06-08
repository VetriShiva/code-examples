package com.vetri.poc.spring.cloud.task.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.cloud.task.listener.annotation.AfterTask;
import org.springframework.cloud.task.listener.annotation.BeforeTask;
import org.springframework.cloud.task.listener.annotation.FailedTask;
import org.springframework.cloud.task.repository.TaskExecution;

@Slf4j
@SpringBootApplication
@EnableTask
public class SpringCloudTaskPocApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudTaskPocApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if(args.length>0)
            log.info("Welcome to {}", args[0]);
        else
            log.info("Welcome to VetriShivaWorld");

    }

    @BeforeTask
    public void start(TaskExecution taskExecution) {
        log.info("TaskName : {} Execution Id : {} - started...", taskExecution.getTaskName() ,taskExecution.getExecutionId());
    }

    @AfterTask
    public void end(TaskExecution taskExecution) {
        log.info("TaskName : {} Execution Id : {} - completed...", taskExecution.getTaskName() ,taskExecution.getExecutionId());
    }

    @FailedTask
    public void fail(TaskExecution taskExecution, Throwable throwable) {
        log.info("TaskName : {} Execution Id : {} - failed...", taskExecution.getTaskName() ,taskExecution.getExecutionId());
    }

}
