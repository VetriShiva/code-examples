package com.vetri.poc.hzcache.test2;

import org.springframework.cache.Cache;
import org.springframework.cache.support.NoOpCache;
import org.springframework.cache.support.SimpleCacheManager;

public class SwitchableSimpleCacheManager extends SimpleCacheManager {

    private boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    /**
     * If the enabled value changes, all caches are cleared
     *
     * @param enabled true or false
     */
    public void setEnabled(boolean enabled) {
        if (enabled != this.enabled) {
            clearCaches();
        }
        this.enabled = enabled;
    }

    @Override
    public Cache getCache(String name) {
        if (enabled) {
            return super.getCache(name);
        } else {
            return new NoOpCache(name);
        }
    }

    protected void clearCaches() {
        this.loadCaches().forEach(cache -> cache.clear());
    }
}