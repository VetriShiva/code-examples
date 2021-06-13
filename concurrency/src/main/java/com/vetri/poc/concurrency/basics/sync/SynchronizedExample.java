package com.vetri.poc.concurrency.basics.sync;

import lombok.extern.slf4j.Slf4j;

/**
 * Example of synchronized operator using.
 * <p>
 * Illustrates monitor working: other threads will wait
 * while current finishes his work.
 */
@Slf4j
public class SynchronizedExample {

    public static void main(final String... args) {
        log.info("Main thread is start working...");
        final CommonResource commonResource = new CommonResource();
        new Thread(new MyRunnable(commonResource)).start();
        new Thread(new MyRunnable(commonResource)).start();
        log.info("Main thread is stop working...");
    }
}
