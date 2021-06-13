package com.vetri.poc.concurrency.basics.sync.statics;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyThread extends Thread {

    @Override
    public void run() {
        log.info("run start");
        CommonStaticResource.increment();
    }
}
