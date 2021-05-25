package com.vetri.poc.kafka.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public final class ConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerService.class);

    @KafkaListener(id = "${kafka.listener.name}",
            topics = "${kafka.topic}", groupId = "${kafka.consumerGroupId}",
            concurrency = "5")
//    @KafkaListener(topics = {"${topic.name}" },
//            clientIdPrefix = "${kafka.client.id.prefix}",
//            idIsGroup = false, id = "${kafka.listener.name}",
//            containerFactory = "kafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String,String> record, Acknowledgment acknowledgment) {
        acknowledgment.acknowledge();
        String key = record.key();
        String message = record.value();
        int partition = record.partition();
        Long offset = record.offset();
        String topic = record.topic();
        logger.info(String.format(
                "Topic: %s, Partition: %s, Offset: %s, Key: %s, Value: %s"
                , topic,partition,offset,key,message));
        //process(10000);
    }

    private void process(long waitMs){
        try {
            Thread.sleep(waitMs);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }
}
