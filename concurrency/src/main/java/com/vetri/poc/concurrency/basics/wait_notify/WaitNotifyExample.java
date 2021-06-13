package com.vetri.poc.concurrency.basics.wait_notify;

/**
 * Example of user {@link Object} methods wait and notify.
 */
public class WaitNotifyExample {

    public static void main(final String... args) {
        final Store store = new Store();
        new Producer(store).start();
        new Consumer(store).start();
    }
}
