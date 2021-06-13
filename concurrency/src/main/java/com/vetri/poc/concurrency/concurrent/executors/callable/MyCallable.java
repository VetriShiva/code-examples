package com.vetri.poc.concurrency.concurrent.executors.callable;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

/**
 * Callable is simple interface, similar with {@link Runnable}
 * but with possibility to return Objects and thrown Exceptions.
 */
@Slf4j
public class MyCallable implements Callable<String> {

    @Override
    public String call() throws Exception {
        log.info("Callable is running and sleep to 2 seconds");
        Thread.sleep(2000);
        return Thread.currentThread().getName();
    }
}
