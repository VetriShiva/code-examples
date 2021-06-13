package com.vetri.poc.concurrency.concurrent.executor_services.schedule;

import com.vetri.poc.concurrency.concurrent.executors.callable.MyCallable;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Example of usage {@link java.util.concurrent.ScheduledExecutorService}.
 */
@Slf4j
public class ScheduleExample {

    public static void main(String[] args) {
        final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        final FutureTask<String> futureTask = new FutureTask<>(new MyCallable());

        scheduledExecutorService.schedule(futureTask, 5, TimeUnit.SECONDS);

        try {
            log.info("Thread: {} has finished.", futureTask.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            scheduledExecutorService.shutdown();
        }
    }
}
