package com.vetri.poc.concurrency.concurrent.executors.callable;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Example of using {@link java.util.concurrent.Callable} interface
 * of java.util.concurrent
 */
@Slf4j
public class CallableExample {

    public static void main(final String[] args) {
        callableSimpleExample();
        callableWithExecutorsExample();
    }

    private static void callableSimpleExample() {
        final Callable<String> callable = new MyCallable();
        final FutureTask<String> futureTask = new FutureTask<>(callable);

        //start callable in new thread
        new Thread(futureTask).start();

        //this line will wait while thread finished his work
        try {
            log.info("Thread: %s has finished.", futureTask.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static void callableWithExecutorsExample() {
        final ExecutorService executor = Executors.newFixedThreadPool(10);
        final List<Future<String>> list = new ArrayList<>();
        final Callable<String> callable = new MyCallable();
        for (int i = 0; i < 20; i++) {
            final Future<String> future = executor.submit(callable);
            list.add(future);
        }
        for (final Future<String> fut : list) {
            try {
                // Future.get() will work with 1 second pause cause of callable implementation
                log.info(new Date() + "::" + fut.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
    }
}
