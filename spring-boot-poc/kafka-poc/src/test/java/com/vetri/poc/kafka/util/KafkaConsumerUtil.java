package com.vetri.poc.kafka.util;

import static org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG;

import java.util.HashMap;
import java.util.Map;

import com.vetri.poc.kafka.impl.CustomAckMessageListener;
import com.vetri.poc.kafka.impl.CustomMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;


/** @author "Bikas Katwal" 13/03/19 */
@Slf4j
public final class KafkaConsumerUtil {

    private static Map<String, ConcurrentMessageListenerContainer<String, String>> consumersMap =
            new HashMap<>();

    /**
     * 1. This method first checks if consumers are already created for a given topic name 2. If the
     * consumers already exists in Map, it will just start the container and return 3. Else create a
     * new consumer and add to the Map
     *
     * @param topic topic name for which consumers is needed
     * @param messageListener pass implementation of MessageListener or AcknowledgingMessageListener
     *     based on enable.auto.commit
     * @param concurrency number of consumers you need
     * @param consumerProperties all the necessary consumer properties need to be passed in this
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void startOrCreateConsumers(
            final String topic,
            final Object messageListener,
            final int concurrency,
            final Map<String, Object> consumerProperties) {

        log.info("creating kafka consumer for topic {}", topic);

        ConcurrentMessageListenerContainer<String, String> container = consumersMap.get(topic);
        if (container != null) {
            if (!container.isRunning()) {
                log.info("Consumer already created for topic {}, starting consumer!!", topic);
                container.start();
                log.info("Consumer for topic {} started!!!!", topic);
            }
            return;
        }

        ContainerProperties containerProps = new ContainerProperties(topic);

        containerProps.setPollTimeout(100);
        Boolean enableAutoCommit = (Boolean) consumerProperties.get(ENABLE_AUTO_COMMIT_CONFIG);
        if (!enableAutoCommit) {
            containerProps.setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        }

        ConsumerFactory<String, String> factory = new DefaultKafkaConsumerFactory<>(consumerProperties);

        container = new ConcurrentMessageListenerContainer<>(factory, containerProps);

        if (enableAutoCommit && !(messageListener instanceof CustomMessageListener)) {
            throw new IllegalArgumentException(
                    "Expected message listener of type com.bkatwal.kafka.impl.CustomMessageListener!");
        }

        if (!enableAutoCommit && !(messageListener instanceof CustomAckMessageListener)) {
            throw new IllegalArgumentException(
                    "Expected message listener of type com.bkatwal.kafka.impl.CustomAckMessageListener!");
        }

        container.setupMessageListener(messageListener);

        if (concurrency == 0) {
            container.setConcurrency(1);
        } else {
            container.setConcurrency(concurrency);
        }

        container.start();

        consumersMap.put(topic, container);

        log.info("created and started kafka consumer for topic {}", topic);
    }

    /**
     * Get the ListenerContainer from Map based on topic name and call stop on it, to stop all
     * consumers for given topic
     *
     * @param topic topic name to stop corresponding consumers
     */
    public static void stopConsumer(final String topic) {
        log.info("stopping consumer for topic {}", topic);
        ConcurrentMessageListenerContainer<String, String> container = consumersMap.get(topic);
        container.stop();
        log.info("consumer stopped!!");
    }

    private KafkaConsumerUtil() {
        throw new UnsupportedOperationException("Can not instantiate KafkaConsumerUtil");
    }
}
