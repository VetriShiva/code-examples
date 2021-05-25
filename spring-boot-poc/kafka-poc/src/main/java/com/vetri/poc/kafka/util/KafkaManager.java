package com.vetri.poc.kafka.util;

import org.apache.kafka.clients.admin.AdminClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class KafkaManager {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaManager.class);

    private final KafkaListenerEndpointRegistry registry;
    private final AdminClient adminClient;

    public KafkaManager(KafkaListenerEndpointRegistry registry, AdminClient adminClient) {
        this.registry = registry;
        this.adminClient=adminClient;
    }

    public List<String> getTopics(){
        List<String> topics = new ArrayList<>();
        try {
            topics = adminClient.listTopics().listings().get().stream().map(s->s.name()).collect(Collectors.toList());
        } catch (Exception e) {
            LOG.error("error in getTopics : "+e.getMessage());
        }
        return topics;
    }

    public List<String> getConsumerGroupIds(){
        List<String> consumerGroupIds = new ArrayList<>();
        try {
            consumerGroupIds = adminClient.listConsumerGroups().all().get().stream().map(s->s.groupId()).collect(Collectors.toList());
        } catch (Exception e) {
            LOG.error("error in getConsumerGroupIds : "+e.getMessage());
        }
        return consumerGroupIds;
    }
    public Set<String> getListenerContainerIds(){
        return registry.getListenerContainerIds();
    }

    public void startListenerContainer(String listenerContainerId) {
        Assert.hasText(listenerContainerId,"listenerContainerId must not be empty");
        if(getListenerContainerIds().contains(listenerContainerId)){
            MessageListenerContainer listenerContainer = registry.getListenerContainer(listenerContainerId);
            if(!listenerContainer.isRunning())
                listenerContainer.start();
        }
    }

    public void pauseListenerContainer(String listenerContainerId) {
        Assert.hasText(listenerContainerId,"listenerContainerId must not be empty");
        if(getListenerContainerIds().contains(listenerContainerId)){
            MessageListenerContainer listenerContainer = registry.getListenerContainer(listenerContainerId);
            if(listenerContainer.isRunning())
                listenerContainer.pause();
        }
    }

    public void stopListenerContainer(String listenerContainerId) {
        Assert.hasText(listenerContainerId,"listenerContainerId must not be empty");
        if(getListenerContainerIds().contains(listenerContainerId)){
            MessageListenerContainer listenerContainer = registry.getListenerContainer(listenerContainerId);
            if(listenerContainer.isRunning())
                listenerContainer.stop();
        }
    }

    public void pause() {
        registry.getListenerContainers().forEach(MessageListenerContainer::pause);
    }
    public void resume() {
        registry.getListenerContainers().forEach(MessageListenerContainer::resume);
    }
}