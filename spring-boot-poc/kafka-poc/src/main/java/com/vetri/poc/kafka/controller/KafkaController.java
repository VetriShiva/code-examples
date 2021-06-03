package com.vetri.poc.kafka.controller;

import com.vetri.poc.kafka.service.ProducerService;
import com.vetri.poc.kafka.util.KafkaManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/kafka")
public final class KafkaController {
    private final ProducerService producerService;

    @Autowired
    KafkaManager kafkaManager;
    @Autowired
    private KafkaListenerEndpointRegistry registry;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public KafkaController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @GetMapping(value = "/")
    public ResponseEntity<Map<String,Object>> getKafkaDetails() {
        Map<String,Object> data = new LinkedHashMap<>();
        data.put("Topics",kafkaManager.getTopics());
        data.put("ConsumerGroupIds",kafkaManager.getConsumerGroupIds());
//        data.put("ListenerContainerIds",kafkaManager.getListenerContainerIds());

        List<String> listenerContainerIds = new ArrayList<>();
        kafkaManager.getListenerContainerIds().stream().forEach(listenerContainerId -> {
            listenerContainerIds.add("listenerContainerId: "+listenerContainerId+", isRunning: "+registry.getListenerContainer(listenerContainerId).isRunning());
        });
        data.put("listenerContainerIds",listenerContainerIds);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping(value = "/publish")
    public String sendMessageToKafkaTopic(@RequestParam String message) {
        for(int i=0;i<50;i++)
            producerService.sendMessage(message+" - "+(i+1));
        return "V";
    }

    @GetMapping(value = "/manager/pause")
    public String pause() {
        kafkaManager.pause();
        return "V-pause";
    }

    @GetMapping(value = "/manager/resume")
    public String resume() {
        kafkaManager.resume();
        return "V-resume";
    }

    @GetMapping(value = "/hello/publish")
    public String sendMessageToHelloTopic(@RequestParam String message) {
        kafkaTemplate.send("hello","V",message);
        return "V";
    }
}
