package com.vetri.poc.kafka.test;


import com.vetri.poc.kafka.api.IMessageProcessor;
import com.vetri.poc.kafka.impl.CustomAckMessageListener;
import com.vetri.poc.kafka.util.KafkaConsumerUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,

        SecurityAutoConfiguration.class,  ManagementWebSecurityAutoConfiguration.class
})
public class Test1 {
    public static void main(String[] args) {
        SpringApplication.run(Test1.class, args);
        System.out.println("2");
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        System.out.println("1");

        Map<String, Object> props = new HashMap<>();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test-group");
        props.put("enable.auto.commit", false);
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        // create your own message processor, this will have processing logic and can save to one or
        // more target DB
        ConcreteMessageProcessor sampleCustomJsonConverter = new ConcreteMessageProcessor();
        CustomAckMessageListener customAckMessageListener = new CustomAckMessageListener(sampleCustomJsonConverter);

        KafkaConsumerUtil.startOrCreateConsumers("test-topic", customAckMessageListener, 1, props);

        sampleProducer("localhost:9092");

        //KafkaConsumerUtil.stopConsumer("test-topic");
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
        for (int i = 0; i < 5; i++)
            producer.send(
                    new ProducerRecord<>("test-topic", Integer.toString(i), "{\"name\":\"name - "+i+"\"}"));

        producer.close();
    }


    static class ConcreteMessageProcessor implements IMessageProcessor {

        @Override
        public void process(String key, String json) {
            System.out.println("key : "+key+" ,json : "+json);
        }
    }
}
