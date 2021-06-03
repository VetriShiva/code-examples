package com.vetri.poc.kafka.common;

import com.vetri.poc.kafka.model.ConsumerInfo;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;

public abstract class AbstractGenericKafkaListener implements GenericKafkaListener {

    private final ConsumerInfo consumerInfo;

    public AbstractGenericKafkaListener(final ConsumerInfo consumerInfo) {
        this.consumerInfo = consumerInfo;
    }

    @Override
    public void handleMessage(final ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        //do common logic here
        specificLogic(record,acknowledgment);
    }

    protected abstract void specificLogic(ConsumerRecord<String, String> record,Acknowledgment acknowledgment);

    public ConsumerInfo getConsumerInfo() {
        return consumerInfo;
    }
}