package com.vetri.poc.concurrency.concurrent.synchronizers.semaphore.parking;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;

@Slf4j
public class Car implements Runnable {

    private static final int PARKING_PLACES_COUNT = 5;
    private static final boolean[] PARKING_PLACES = new boolean[PARKING_PLACES_COUNT];
    private static final Semaphore SEMAPHORE = new Semaphore(PARKING_PLACES_COUNT, true);

    private final int carNumber;

    public Car(final int carNumber) {
        this.carNumber = carNumber;
    }

    @Override
    public void run() {
        log.info("The car No {} drove up to the parking", carNumber);
        try {
            SEMAPHORE.acquire();
            int parkingNumber = -1;
            synchronized (PARKING_PLACES) {
                for (int i = 0; i < 5; i++)
                    if (!PARKING_PLACES[i]) {
                        PARKING_PLACES[i] = true;
                        parkingNumber = i;
                        log.info("The car No {} has parked at the place {}", carNumber, i);
                        break;
                    }
            }

            Thread.sleep(5000);

            synchronized (PARKING_PLACES) {
                PARKING_PLACES[parkingNumber] = false;
            }

            SEMAPHORE.release();
            log.info("The car No {} has left the parking.", carNumber);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
