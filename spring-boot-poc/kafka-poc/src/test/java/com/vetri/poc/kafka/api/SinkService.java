package com.vetri.poc.kafka.api;

public interface SinkService {

    boolean update(String jsonData);

    boolean update(String targetName, String jsonData);
}
