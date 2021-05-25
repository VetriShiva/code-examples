package com.vetri.poc.hzcache.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastCacheConfig {

    @Bean
    public CacheManager hazelcastCacheManager(@Autowired HazelcastInstance hazelcastInstance) {
        return new HazelcastCacheManager(hazelcastInstance);
    }

    @Bean
    public ClientConfig clientConfig() {
        ClientConfig clientConfig = new ClientConfig();
        ClientNetworkConfig networkConfig = clientConfig.getNetworkConfig();
        networkConfig.addAddress("localhost:5701", "localhost:5701")
                .setSmartRouting(true)
                .addOutboundPortDefinition("34700-34710")
                .setRedoOperation(true)
                .setConnectionTimeout(5000);

        return clientConfig;

    }

    @Bean
    public HazelcastInstance hazelcastInstance(ClientConfig clientConfig) {
        return HazelcastClient.newHazelcastClient(clientConfig);
    }
}
