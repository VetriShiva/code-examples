package com.vetri.poc.kafka.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@ConfigurationProperties(prefix = "consumer-list")
@Configuration
@Component
@Data
public class ConsumerInfoList {
    private List<ConsumerInfo> configurations;
}
