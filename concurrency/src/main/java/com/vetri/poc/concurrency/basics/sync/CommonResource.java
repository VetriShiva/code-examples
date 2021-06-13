package com.vetri.poc.concurrency.basics.sync;

public class CommonResource {
    private int counter = 0;

    public void increment() {
        counter++;
    }

    public int getCounter() {
        return counter;
    }
}
