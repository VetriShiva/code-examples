package com.vetri.poc.kafka.impl;

import com.vetri.poc.kafka.api.IMessageProcessor;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;

/** @author "Bikas Katwal" 13/03/19 */
@AllArgsConstructor
public class CustomMessageListener implements MessageListener<String, String> {

    // inject your own concrete processor
    private IMessageProcessor messageProcessor;

    @Override
    public void onMessage(ConsumerRecord<String, String> consumerRecord) {

        // process message
        messageProcessor.process(consumerRecord.key(), consumerRecord.value());
    }
}
