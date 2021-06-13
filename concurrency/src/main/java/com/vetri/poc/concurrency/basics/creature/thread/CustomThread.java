package com.vetri.poc.concurrency.basics.creature.thread;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomThread extends Thread {
    @Override
    public void run() {
        log.info("Thread Task");
    }
}
