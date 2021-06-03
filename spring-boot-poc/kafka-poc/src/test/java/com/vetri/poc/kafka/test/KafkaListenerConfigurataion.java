package com.vetri.poc.kafka.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListenerConfigurer;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
public class KafkaListenerConfigurataion implements KafkaListenerConfigurer {

    private final List<AbstractGenericKafkaListener> listeners;
    private final BeanFactory beanFactory;
    private final MessageHandlerMethodFactory messageHandlerMethodFactory;
    private final KafkaListenerContainerFactory kafkaListenerContainerFactory;

    private String consumerGroup;
    private String service;

    public KafkaListenerConfigurataion(
            List<AbstractGenericKafkaListener> listeners,BeanFactory beanFactory,
            MessageHandlerMethodFactory messageHandlerMethodFactory,KafkaListenerContainerFactory kafkaListenerContainerFactory,
            @Value("${your.kafka.consumer.group-id}") String consumerGroup,
            @Value("${your.application.name}") String service
    ) {
        this.listeners=listeners;
        this.beanFactory=beanFactory;
        this.messageHandlerMethodFactory=messageHandlerMethodFactory;
        this.kafkaListenerContainerFactory=kafkaListenerContainerFactory;
        this.consumerGroup=consumerGroup;
        this.service=service;
    }

    @Override
    public void configureKafkaListeners(
            final KafkaListenerEndpointRegistrar registrar) {

        final Method listenerMethod = lookUpMethod();

        listeners.forEach(listener -> {
            registerListenerEndpoint(listener, listenerMethod, registrar);
        });
    }

    private void registerListenerEndpoint(final AbstractGenericKafkaListener listener,
                                          final Method listenerMethod,
                                          final KafkaListenerEndpointRegistrar registrar) {

        log.info("Registering {} endpoint on topic {}", listener.getClass(),
                listener.getKafkaTopic());

        final MethodKafkaListenerEndpoint<String, String> endpoint =
                createListenerEndpoint(listener, listenerMethod);
        registrar.registerEndpoint(endpoint);
    }

    private MethodKafkaListenerEndpoint<String, String> createListenerEndpoint(
            final AbstractGenericKafkaListener listener, final Method listenerMethod) {

        final MethodKafkaListenerEndpoint<String, String> endpoint = new MethodKafkaListenerEndpoint<>();
        endpoint.setBeanFactory(beanFactory);
        endpoint.setBean(listener);
        endpoint.setMethod(listenerMethod);
        endpoint.setId(service + "-" + listener.getKafkaTopic());
        endpoint.setGroup(consumerGroup);
        endpoint.setTopics(listener.getKafkaTopic());
        endpoint.setMessageHandlerMethodFactory(messageHandlerMethodFactory);

        return endpoint;
    }

    private Method lookUpMethod() {
        return Arrays.stream(GenericKafkaListener.class.getMethods())
                .filter(m -> m.getName().equals(GenericKafkaListener.METHOD))
                .findAny()
                .orElseThrow(() ->
                        new IllegalStateException("Could not find method " + GenericKafkaListener.METHOD));
    }
}
