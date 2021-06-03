package com.vetri.poc.kafka.test;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface GenericKafkaListener {
    String METHOD = "handleMessage";
    void handleMessage(ConsumerRecord<String, String> record);
}
