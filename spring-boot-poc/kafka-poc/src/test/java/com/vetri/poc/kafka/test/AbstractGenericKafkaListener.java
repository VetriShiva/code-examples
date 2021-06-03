package com.vetri.poc.kafka.test;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public abstract class AbstractGenericKafkaListener implements GenericKafkaListener {

    private final String kafkaTopic;

    public AbstractGenericKafkaListener(final String kafkaTopic) {
        this.kafkaTopic = kafkaTopic;
    }

    @Override
    public void handleMessage(final ConsumerRecord<String, String> record) {
        //do common logic here
        specificLogic(record);
    }

    protected abstract void specificLogic(ConsumerRecord<String, String> record);

    public String getKafkaTopic() {
        return kafkaTopic;
    }
}
