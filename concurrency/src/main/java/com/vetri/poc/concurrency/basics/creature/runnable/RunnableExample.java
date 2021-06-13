package com.vetri.poc.concurrency.basics.creature.runnable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RunnableExample {
    public static void main(String[] args) {
        log.info("Main thread is start working...");
        Runnable target;
        Thread task = new Thread(new CustomRunnable());
        task.start();
        log.info("Main thread is stop working...");
    }
}
