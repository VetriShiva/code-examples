package com.vetri.poc.concurrency.basics.wait_notify;

public class Consumer extends Thread {

    private final Store store;

    public Consumer(final Store store) {
        this.store = store;
    }

    @Override
    public void run() {
        for (int i = 1; i < 6; i++) {
            try {
                store.getItem();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
