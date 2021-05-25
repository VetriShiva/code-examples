package com.vetri.poc.hzcache.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CacheService{

	@Cacheable("default")
	public String sayHello(String name){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "hello "+name;
	}

	@Cacheable(value = "hazelcastMap",cacheManager = "hazelcastCacheManager",keyGenerator = "customKeyGenerator")
	public String sayHello1(String name){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "hello "+name;
	}
}
