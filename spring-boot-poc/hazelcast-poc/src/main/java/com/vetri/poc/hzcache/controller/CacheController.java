package com.vetri.poc.hzcache.controller;


import com.hazelcast.client.impl.proxy.ClientMapProxy;
import com.vetri.poc.hzcache.service.CacheService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;

@RequestMapping("/cache")
@RestController
@AllArgsConstructor
class CacheController{

	CacheService cacheService;
	CacheManager cacheManager;
	@Autowired
	@Qualifier("hazelcastCacheManager")
    CacheManager hazelcastCacheManager;

	@RequestMapping("/hello")
	public String sayHello(String name){
		return cacheService.sayHello(name);
	}

	@RequestMapping("/hello1")
	public String sayHello1(String name){
		return cacheService.sayHello1(name);
	}

	@RequestMapping(value = "/print",produces = MediaType.TEXT_PLAIN_VALUE)
	public String printAllCache(){
		StringBuffer info= new StringBuffer();
		info.append("Default Cache Manager\n");
		cacheManager.getCacheNames().stream().forEach(cacheName->{
			info.append(String.format("cacheName=%s\n",cacheName));
			info.append(String.format("cacheType=%s\n",cacheManager.getCache(cacheName).getNativeCache().getClass()));
			info.append(String.format("cacheManagerType=%s\n\n",cacheManager.getClass()));

			ConcurrentHashMap data = (ConcurrentHashMap)cacheManager.getCache(cacheName).getNativeCache();
			data.forEach((k,v)->{
				info.append(String.format("key=%s, value=%s\n",k,v));
			});
		});

		info.append("\n\nHazelcast Cache Manager\n");
		hazelcastCacheManager.getCacheNames().stream().forEach(cacheName->{
			info.append(String.format("cacheName=%s\n",cacheName));
			info.append(String.format("cacheType=%s\n",hazelcastCacheManager.getCache(cacheName).getNativeCache().getClass()));
			info.append(String.format("cacheManagerType=%s\n\n",hazelcastCacheManager.getClass()));

			ClientMapProxy data = (ClientMapProxy)hazelcastCacheManager.getCache(cacheName).getNativeCache();
			data.forEach((k,v)->{
				info.append(String.format("key=%s, value=%s\n",k,v));
			});
		});
		return info.toString();
	}
}
