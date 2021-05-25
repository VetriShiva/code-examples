package com.vetri.poc.hzcache;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/*
	https://medium.com/@thanh_tran/cacheable-annotation-in-spring-d8d347ad72fd
	https://dzone.com/articles/how-to-scale-hazelcast-cluster-with-docker-compose
	https://github.com/hazelcast/hazelcast-code-samples/blob/master/hazelcast-integration/docker-compose/docker-compose.yml

	default cache manager - org.springframework.cache.concurrent.ConcurrentMapCacheManager
 */
@SpringBootApplication
@EnableCaching
public class HazelcastCacheApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(HazelcastCacheApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("App Started...");
	}
}

