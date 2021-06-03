package com.vetri.poc.kafka;


import com.vetri.poc.kafka.api.IMessageProcessor;
import com.vetri.poc.kafka.impl.CustomAckMessageListener;
import com.vetri.poc.kafka.util.KafkaConsumerUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.kafka.test.rule.EmbeddedKafkaRule;

class KafkaPocApplicationTest {
    @ClassRule
    public static EmbeddedKafkaRule embeddedKafkaRule = new EmbeddedKafkaRule(1, true, 1, "test-topic");

    @AfterClass
    public static void close() {
        try {
            embeddedKafkaRule.getEmbeddedKafka().destroy();
        } catch (Exception e) {
            // do nothing
        }
    }

    /**
     * this test will give a demo of the usage of this utility project. Below code needs to be used in
     * the client that uses this project
     */
    @Test
    public void kafkaUtilTest() throws InterruptedException {
        Logger.getRootLogger().setLevel(Level.INFO);
        String broker = embeddedKafkaRule.getEmbeddedKafka().getBrokerAddresses()[0].toString();

        Map<String, Object> props = new HashMap<>();
        props.put("bootstrap.servers", broker);
        props.put("group.id", "test-group");
        props.put("enable.auto.commit", false);
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        // create your own message processor, this will have processing logic and can save to one or
        // more target DB
        ConcreteMessageProcessor sampleCustomJsonConverter = new ConcreteMessageProcessor();
        CustomAckMessageListener customAckMessageListener =
                new CustomAckMessageListener(sampleCustomJsonConverter);

        // start the consumer
        KafkaConsumerUtil.startOrCreateConsumers("test-topic", customAckMessageListener, 1, props);

        // waiting for consumer to start and partition assignment
        System.out.println("waiting for consumer to start and partition assignment");
        //Thread.sleep(30000);
        sampleProducer(broker);

        // wait for consumer to finish consuming data
        System.out.println("wait for consumer to finish consuming data");
        //Thread.sleep(10000);

        // stop the consumer
        KafkaConsumerUtil.stopConsumer("test-topic");
    }

    private void sampleProducer(String bootstrapServer) {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServer);
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 2);
        props.put("linger.ms", 1);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);
        for (int i = 0; i < 100; i++)
            producer.send(
                    new ProducerRecord<>("test-topic", Integer.toString(i), "{\"name\":\"bikas\"}"));

        producer.close();
    }

    static class ConcreteMessageProcessor implements IMessageProcessor{

        @Override
        public void process(String key, String json) {
            System.out.println("key : "+key);
            System.out.println("json : "+json);
        }
    }
}