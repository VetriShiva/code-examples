package com.vetri.poc.kafka.service;

import com.vetri.poc.kafka.common.AbstractGenericKafkaListener;
import com.vetri.poc.kafka.model.ConsumerInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;

@Slf4j
public class CommonGenericKafkaListener extends AbstractGenericKafkaListener {

    public CommonGenericKafkaListener(ConsumerInfo consumerInfo) {
        super(consumerInfo);
    }

    @Override
    protected void specificLogic(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        if(!super.getConsumerInfo().isEnableAutoCommit() &&
            "start".equals(super.getConsumerInfo().getAutoCommitCodePosition())) {
            log.info("acknowledge before process");
            acknowledgment.acknowledge();
        }

        log.info("Topic: {}, Key: {}, Partition: {}, Offset: {}, Value: {}",
                record.topic(),record.key(),record.partition(),record.offset(),record.value());

        log.info("Configurable Message : {}",super.getConsumerInfo().getMessage());

        if(!super.getConsumerInfo().isEnableAutoCommit() &&
                !"start".equals(super.getConsumerInfo().getAutoCommitCodePosition())) {
            log.info("acknowledge after process");
            acknowledgment.acknowledge();
        }
    }
}
