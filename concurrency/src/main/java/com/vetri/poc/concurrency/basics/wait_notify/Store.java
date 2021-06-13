package com.vetri.poc.concurrency.basics.wait_notify;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Store {

    private int item = 0;

    public synchronized void getItem() throws InterruptedException {
        while (item < 1) {
            wait();
            Thread.sleep(1000);
        }
        item--;
        log.info("Buyer bought 1 item");
        log.info("Goods in stock: " + item);
        notify();
    }

    public synchronized void putItem() throws InterruptedException {
        while (item >= 5) {
            wait();
            Thread.sleep(1000);
        }
        item++;
        Thread.sleep(1000);
        log.info("Manufacturer added 1 product");
        log.info("Goods in stock: " + item);
        notify();
    }
}
