package com.vetri.poc.kafka.common;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;

public interface GenericKafkaListener {
    String METHOD = "handleMessage";
    void handleMessage(ConsumerRecord<String, String> record, Acknowledgment acknowledgment);
}
