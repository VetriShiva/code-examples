package com.vetri.poc.kafka.common;

import com.vetri.poc.kafka.model.ConsumerInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListenerConfigurer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
public class KafkaListenerConfiguration implements KafkaListenerConfigurer {

    private final List<AbstractGenericKafkaListener> listeners;

    private final BeanFactory beanFactory;

    private final MessageHandlerMethodFactory messageHandlerMethodFactory;

    public KafkaListenerConfiguration(List<AbstractGenericKafkaListener> listeners, BeanFactory beanFactory, MessageHandlerMethodFactory messageHandlerMethodFactory) {
        this.listeners = listeners;
        this.beanFactory = beanFactory;
        this.messageHandlerMethodFactory = messageHandlerMethodFactory;
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
                listener.getConsumerInfo().getTopics());

        final MethodKafkaListenerEndpoint<String, String> endpoint = createListenerEndpoint(listener, listenerMethod);
        String containerFactoryBeanId = listener.getConsumerInfo().getContainerFactory();

        boolean containerFactoryBeanExists = true;
        try{
            beanFactory.getBean(containerFactoryBeanId);
        }catch (NoSuchBeanDefinitionException e){
            containerFactoryBeanExists=false;
        }
        if(!containerFactoryBeanExists){
            log.info("Bean with this id {} not exists!",containerFactoryBeanId);
            Assert.state(beanFactory instanceof ConfigurableBeanFactory, "wrong bean factory type");
            ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory)beanFactory;

            KafkaListenerContainerFactory kafkaListenerContainerFactory = createListenerContainerFactory(listener);
            configurableBeanFactory.registerSingleton(containerFactoryBeanId,kafkaListenerContainerFactory);
        }
        registrar.registerEndpoint(endpoint,beanFactory.getBean(containerFactoryBeanId,KafkaListenerContainerFactory.class));
    }

    private KafkaListenerContainerFactory createListenerContainerFactory(AbstractGenericKafkaListener listener) {
        ConsumerInfo consumerInfo = listener.getConsumerInfo();

        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerInfo.getBootstrapServers());
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, consumerInfo.getGroupId());
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, consumerInfo.getKeyDeserializerClass());
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, consumerInfo.getValueDeserializerClass());
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES,consumerInfo.getTrustedPackages());
        configProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, consumerInfo.getMaxPollRecords());
        configProps.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, consumerInfo.getMaxPollIntervalMs());
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumerInfo.getAutoOffsetReset());

        configProps.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, consumerInfo.getSessionTimeoutMs());
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, consumerInfo.isEnableAutoCommit());
        if(consumerInfo.isEnableAutoCommit())
            configProps.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, consumerInfo.getAutoCommitIntervalMs());

        ConsumerFactory<String,String> consumerFactory = new DefaultKafkaConsumerFactory<>(configProps);
        ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        kafkaListenerContainerFactory.setConsumerFactory(consumerFactory);
        if(!consumerInfo.isEnableAutoCommit())
            kafkaListenerContainerFactory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        return kafkaListenerContainerFactory;
    }

    private MethodKafkaListenerEndpoint<String, String> createListenerEndpoint(
            final AbstractGenericKafkaListener listener, final Method listenerMethod) {
        ConsumerInfo consumerInfo = listener.getConsumerInfo();

        final MethodKafkaListenerEndpoint<String, String> endpoint = new MethodKafkaListenerEndpoint<>();
        endpoint.setBeanFactory(beanFactory);
        endpoint.setBean(listener);
        endpoint.setMethod(listenerMethod);
        endpoint.setId(consumerInfo.getId());
        endpoint.setTopics(consumerInfo.getTopics());
        endpoint.setGroup(consumerInfo.getGroupId());
        endpoint.setConcurrency(consumerInfo.getConcurrency());
        endpoint.setAutoStartup(consumerInfo.isAutoStartup());
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
