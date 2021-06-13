package com.vetri.poc.concurrency.concurrent.collections;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Concurrent collections examples.
 */
@Slf4j
public class ConcurrencyCollectionsExample {

    public static void main(final String[] args) {
//        copyOnWriteExample();
        mapExample();

    }

    /**
     * Thread-safe analogue of ArrayList and Set
     * All modifying operations (add, set, remove) will create new copy of collection.
     * <p>
     * {@link CopyOnWriteArrayList} has additional methods unlike the {@link CopyOnWriteArraySet}.
     */
    public static void copyOnWriteExample() {
        final CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        // using equals method
        copyOnWriteArrayList.addIfAbsent("string");
        // start search from given index
        copyOnWriteArrayList.indexOf("string", 0);

        final CopyOnWriteArraySet<String> copyOnWriteArraySet = new CopyOnWriteArraySet<>();
    }

    /**
     * {@link java.util.concurrent.ConcurrentMap} implementations instead of {@link java.util.HashMap}
     * and {@link java.util.TreeMap} with better support for multithreading and scalability
     */
    public static void mapExample() {
        // Data are represented as segments broken by hash of keys
        final ConcurrentMap<String, String> concurrentHashMap = new ConcurrentHashMap<>();

        concurrentHashMap.putIfAbsent("key", "value");
        concurrentHashMap.remove("key", "value");
        concurrentHashMap.replace("key", "value", "newValue");
        concurrentHashMap.replace("key", "newValue");

        // Thread-safe analogue of TreeMap with multithreading support
        final ConcurrentMap<String, String> concurrentSkipListMap = new ConcurrentSkipListMap<>();
    }
}
