package com.vetri.poc.concurrency.concurrent.synchronizers.countdawnlatch;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

/**
 * Example of using {@link java.util.concurrent.CountDownLatch}
 */
@Slf4j
public class CountDawnLatchExample {

    public static void main(final String... args) throws InterruptedException {
        final int invokeTimes = 3;
        final CountDownLatch countDownLatch = new CountDownLatch(invokeTimes);

        log.info(Thread.currentThread().getName() + " was stared");

        for (int counter = 0; counter < invokeTimes; counter++) {
            new Work(countDownLatch);
        }

        countDownLatch.await();

        log.info(Thread.currentThread().getName() + " finished");
    }
}