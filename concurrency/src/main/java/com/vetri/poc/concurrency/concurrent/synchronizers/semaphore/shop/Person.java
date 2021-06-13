package com.vetri.poc.concurrency.concurrent.synchronizers.semaphore.shop;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;

/**
 * Custom {@link Thread} implementation with person queue in store.
 */
@Slf4j
public class Person extends Thread {

    private final Semaphore cashbox;

    Person(final Semaphore cashbox) {
        this.cashbox = cashbox;
    }

    @Override
    public void run() {
        log.info("{} waiting for order in shop", this.getName());
        try {
            cashbox.acquire();
            log.info(this.getName() + " buying products");
            Thread.sleep(2000);
            cashbox.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
