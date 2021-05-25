package com.vetri.poc.hzcache.test1;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@EnableCaching
//@SpringBootApplication
public class SampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

    @Bean
    public ClientConfig clientConfig(){
        ClientConfig clientConfig= new ClientConfig();
        clientConfig.getNetworkConfig().addAddress("172.19.128.1:5701");
//        ClientStateListener clientStateListener = new ClientStateListener(clientConfig);
        return clientConfig;
    }

    @Bean("hazelcastInstance")
    public HazelcastInstance hazelcastInstance(ClientConfig clientConfig){
        HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);
        hazelcastInstance.getLifecycleService().addLifecycleListener(event -> System.out.println(">>>>>>>>>> LIFECYCLE State changed: " + event));
        return hazelcastInstance;
    }

//    @Bean
//    public CacheManager cacheManager(@Autowired @Qualifier("hazelcastInstance") HazelcastInstance hazelcastInstance){
//        return new HazelcastCacheManager(hazelcastInstance);
//    }

    @Configuration
    class MyCachingConfigurer implements CachingConfigurer {

        @Autowired
        @Qualifier("hazelcastInstance")
        HazelcastInstance hazelcastInstance;

        @Bean
        @Override
        public CacheManager cacheManager() {
//            SimpleCacheManager cacheManager = new SimpleCacheManager();
//            cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("books")));
//            return cacheManager;

            return cacheManager(hazelcastInstance);
        }

        public CacheManager cacheManager(HazelcastInstance hazelcastInstance){
            return new HazelcastCacheManager(hazelcastInstance);
        }

        @Override
        public CacheResolver cacheResolver() {
            return null;
        }

        @Override
        public KeyGenerator keyGenerator() {
            return null;
        }

        @Override
        public CacheErrorHandler errorHandler() {
            return new CustomCacheErrorHandler();
        }
    }
}


class CustomCacheErrorHandler implements CacheErrorHandler {
    @Override
    public void handleCacheGetError(RuntimeException exception,
                                    Cache cache, Object key) {
        //Do something on Get Error in cache
    }
    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache,
                                    Object key, Object value) {
        //Do something on Put error in cache
    }
    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache,
                                      Object key) {
        //Do something on error while Evict cache
    }
    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache){
        //Do something on error while clearing cache
    }
}

@RestController
@RequestMapping("/books")
class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/{isbn}")
    public String getBookNameByIsbn(@PathVariable("isbn") String isbn) {
        return bookService.getBookNameByIsbn(isbn);
    }
}

@Service
class BookService {
    @Cacheable("books")
    public String getBookNameByIsbn(String isbn) {
        return findBookInSlowSource(isbn);
    }

    private String findBookInSlowSource(String isbn) {
        // some long processing
        System.out.println("From datasource");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Sample Book Name";
    }
}
