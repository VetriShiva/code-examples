package com.vetri.poc.kafka.api;

public interface IMessageProcessor {

    void process(String key, String json);
}