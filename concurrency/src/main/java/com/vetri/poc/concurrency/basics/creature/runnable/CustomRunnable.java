package com.vetri.poc.concurrency.basics.creature.runnable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomRunnable implements Runnable {
    @Override
    public void run() {
        log.info("Thread Task");
    }
}
