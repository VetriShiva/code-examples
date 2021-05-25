package com.vetri.hellocache.config;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DefaultCacheConfig {
    // default cache manager
    @Bean
    @Primary
    public CacheManager cacheManager() {
        return new org.springframework.cache.concurrent.ConcurrentMapCacheManager();
    }
}
