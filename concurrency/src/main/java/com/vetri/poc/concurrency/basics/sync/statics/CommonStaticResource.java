package com.vetri.poc.concurrency.basics.sync.statics;

import lombok.extern.slf4j.Slf4j;

/**
 * Synchronized {@link CommonStaticResource} class.
 * <p>
 * Creates monitor, so any thread will wait while
 * current thread finish job ang go to dead state.
 */
@Slf4j
public class CommonStaticResource {
    private static int counter = 0;

    public synchronized static void increment() {
        log.info("Start increment. Thread: {}. Counter: {}", Thread.currentThread().getName(), counter);
        CommonStaticResource.counter++;
        log.info("Finish increment. Thread: {}. Counter: {}", Thread.currentThread().getName(), counter);
    }

//    Equivalent method to previous one.
//    public static void increment() {
//        synchronized (CommonStaticResource.class) {
//            log.info("Start increment. Thread: {}. Counter: {}", Thread.currentThread().getName(), counter);
//            CommonStaticResource.counter++;
//            log.info("Finish increment. Thread: {}. Counter: {}", Thread.currentThread().getName(), counter);
//        }
//    }
}
