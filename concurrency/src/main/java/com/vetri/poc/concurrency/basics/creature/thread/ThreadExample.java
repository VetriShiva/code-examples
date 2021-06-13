package com.vetri.poc.concurrency.basics.creature.thread;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadExample {
    public static void main(final String... args) {
        log.info("Main thread is start working...");

        customThreadExample();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Main thread is stop working...");
    }

    private static void customThreadExample() {
//        new CustomThread().run(); // if use run() thread will join to "main" thread.
        new CustomThread().start(); // start work in another thread
    }
}
