package com.vetri.poc.concurrency.concurrent.synchronizers.semaphore.shop;

import java.util.concurrent.Semaphore;

/**
 * Example of usage {@link java.util.concurrent.Semaphore}.
 */
public class SemaphoreExample {

    public static void main(final String... args) {
        final int maxPermits = 2;
        final Semaphore cashbox = new Semaphore(maxPermits);

        for (int counter = 0; counter < 10; counter++) {
            new Person(cashbox).start();
        }
    }
}
