package com.vetri.poc.concurrency.basics.wait_notify;

public class Producer extends Thread {

    private final Store store;

    public Producer(final Store store) {
        this.store = store;
    }

    @Override
    public void run(){
        for (int i = 1; i < 6; i++) {
            try {
                store.putItem();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
