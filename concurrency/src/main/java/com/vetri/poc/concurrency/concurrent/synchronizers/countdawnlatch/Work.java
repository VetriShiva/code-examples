package com.vetri.poc.concurrency.concurrent.synchronizers.countdawnlatch;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class Work extends Thread {

    private final CountDownLatch countDownLatch;

    public Work(final CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
        start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info(Thread.currentThread().getName() + " finished work");
        countDownLatch.countDown();
    }
}
