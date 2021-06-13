package com.vetri.poc.concurrency.basics.creature;

import lombok.extern.slf4j.Slf4j;

/**
 * Show main class methods to get thread info:
 * <p>
 * getName() - get thread name
 * getPriority() - get thread priority
 * isAlive() - is thread running
 * join() - wait for thread end
 * run() - custom implementation to run
 * sleep() - suspend current thread to n milliseconds
 * start() - start thread
 */
@Slf4j
public class ThreadInfo {

    public static void main(final String... args) {
        showThreadMainInfo(Thread.currentThread());
    }

    private static void showThreadMainInfo(final Thread thread) {
        log.info("Thread.getId: {}",thread.getId()); //1
        log.info("Thread.getName: {}",thread.getName()); // main
        log.info("Thread.getPriority: {}",thread.getPriority()); // Default: 5
        log.info("Thread.getState: {}",thread.getState()); // RUNNABLE
        log.info("Thread.isAlive: {}",thread.isAlive()); // true
        log.info("Thread.isDaemon: {}",thread.isDaemon()); // false
        log.info("Thread.isInterrupted: {}",thread.isInterrupted()); // false
    }
}
