package com.vetri.poc.hzcache.test2;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
public class ConfigTest1 {

    private static final String RESULT_CACHE = "RESULT_CACHE";

    @Bean
    public SwitchableSimpleCacheManager cacheManager() {
        SwitchableSimpleCacheManager cacheManager = new SwitchableSimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
                buildCache(RESULT_CACHE, 24, 5000)
        ));
        return cacheManager;
    }

    private CaffeineCache buildCache(String name, int hoursToExpire, long maxSize) {
        return new CaffeineCache(
                name,
                Caffeine.newBuilder()
                        .expireAfterWrite(hoursToExpire, TimeUnit.HOURS)
                        .maximumSize(maxSize)
                        .build()
        );
    }
}
