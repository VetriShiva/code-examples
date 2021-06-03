package com.vetri.poc.kafka.model;

import lombok.Data;

@Data
public class ConsumerInfo {
    private String listenerBeanName;

    // Consumer Props
    private String bootstrapServers;
    private String groupId;
    private String keyDeserializerClass;
    private String valueDeserializerClass;
    private String trustedPackages;
    private int maxPollRecords;
    private int maxPollIntervalMs;
    private int sessionTimeoutMs;
    private boolean enableAutoCommit; // AckMode will set based on this
    private String autoCommitCodePosition; // start or end of consumer method
    private int autoCommitIntervalMs;
    private String autoOffsetReset;

    // Listener Props
    private String id;
    private String topics;
    private boolean autoStartup;
    private int concurrency;
    private String containerFactory;

    // Custom Props
    private String message;
}
