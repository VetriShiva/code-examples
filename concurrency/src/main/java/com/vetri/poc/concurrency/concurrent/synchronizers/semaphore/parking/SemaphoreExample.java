package com.vetri.poc.concurrency.concurrent.synchronizers.semaphore.parking;

/**
 * Example of usage {@link java.util.concurrent.Semaphore}.
 */
public class SemaphoreExample {

    public static void main(final String... args) throws InterruptedException {
        for (int i = 1; i <= 7; i++) {
            new Thread(new Car(i)).start();
            Thread.sleep(400);
        }
    }
}
