package com.vetri.poc.concurrency.basics.sync;

import com.vetri.poc.concurrency.basics.creature.ThreadInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * Synchronized {@link CommonResource} class.
 * <p>
 * Creates monitor, so any thread will wait while
 * current thread finish job ang go to dead state.
 */
@Slf4j
public class MyRunnable implements Runnable {
    private final CommonResource commonResource;

    public MyRunnable(final CommonResource commonResource) {
        this.commonResource = commonResource;
    }

    @Override
    public void run() {
        log.info("run start");
        synchronized (commonResource) {
            for (int count = 0; count < 3; count++) {
                commonResource.increment();
                log.info("Thread: {}. Counter: {}", Thread.currentThread().getName(), commonResource.getCounter());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }
}
