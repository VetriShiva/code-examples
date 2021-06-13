package com.vetri.poc.concurrency.basics.sync.statics;

/**
 * Example of synchronized operator using with static fields and methods.
 * <p>
 * Illustrates monitor working: other threads will wait
 * while current finishes his work.
 */
public class StaticSynchronizedExample {

    public static void main(final String... args) {
        new MyThread().start();
        new MyThread().start();
    }
}