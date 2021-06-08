package com.vetri.poc.resilience4j.controller;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@RestController
@RequestMapping("/api/resilience4j")
public class HomeController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/")
    public ResponseEntity<Map<String,Object>> home(){
        Map<String,Object> data = new LinkedHashMap<>();
        data.put("Message","Vetri Shiva");
        return new ResponseEntity<Map<String,Object>>(data, HttpStatus.OK);
    }

    @GetMapping("/test/retry") // http://localhost:9094/api/resilience4j/test/retry
    public ResponseEntity<String> testRetry(){
        log.info("testRetry");
        String data = restTemplate.getForObject("http://localhost:9094/api/resilience4j/retry",String.class);
        return new ResponseEntity<String>(data, HttpStatus.OK);
    }

    @GetMapping("/test/circuitBreaker") // http://localhost:9094/api/resilience4j/test/circuitBreaker
    public ResponseEntity<String> testCircuitBreaker()throws Exception{
        log.info("testCircuitBreaker");
        String data = null;
        for(int i=0;i<1000;i++) {
            Thread.sleep(100);
            data = restTemplate.getForObject("http://localhost:9094/api/resilience4j/circuitBreaker", String.class);
            System.out.println(" i: "+(i+1)+" -> "+data);
        }
        return new ResponseEntity<String>(data, HttpStatus.OK);
    }

    @GetMapping("/test/ratelimiter") // http://localhost:9094/api/resilience4j/test/ratelimiter
    public ResponseEntity<String> testRatelimiter() {
        log.info("testRatelimiter");
        String data = null;
        for (int i = 0; i < 15; i++){
            log.info("Call No : {}",(i+1));
            data = restTemplate.getForObject("http://localhost:9094/api/resilience4j/ratelimiter", String.class);
        }
        return new ResponseEntity<String>(data, HttpStatus.OK);
    }

    @GetMapping("/test/bulkhead") // http://localhost:9094/api/resilience4j/test/bulkhead
    public ResponseEntity<String> testBulkhead() {
        log.info("testBulkhead");
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Callable<String>> tasks = new ArrayList<>();
        for(int i = 0; i < 15; i++) {
            tasks.add(()->{
                String data = restTemplate.getForObject("http://localhost:9094/api/resilience4j/bulkhead", String.class);
                return data;
            });
        }
        try {
            executor.invokeAll(tasks);
        } catch(Exception e) {
            e.printStackTrace();
        }
        executor.shutdown();
        return new ResponseEntity<String>("V", HttpStatus.OK);
    }


    @GetMapping("/retry") // http://localhost:9094/api/resilience4j/retry
    @Retry(name = "allCustomer", fallbackMethod = "showError")
    public ResponseEntity<Map<String,Object>> retry() throws Exception {
        log.info("retry");
        Map<String,Object> data = getData(true);
        return new ResponseEntity<Map<String,Object>>(data, HttpStatus.OK);
    }

    @GetMapping("/circuitBreaker") // http://localhost:9094/api/resilience4j/circuitBreaker
    @CircuitBreaker(name = "default", fallbackMethod = "showError")
    public ResponseEntity<Map<String,Object>> simulateCircuitBreaker() {
        log.info("circuitBreaker");
        Map<String,Object> data = getData(true);
        return new ResponseEntity<Map<String,Object>>(data, HttpStatus.OK);
    }

    @GetMapping("/timelimiter") // http://localhost:9094/api/resilience4j/timelimiter
    @TimeLimiter(name = "default")
    public CompletableFuture<Void> timeLimiter() {
        log.info("timeLimiter");
        return CompletableFuture.runAsync(runnable);
    }

    @GetMapping("/ratelimiter")
    @RateLimiter(name = "default")
    public ResponseEntity<String> rateLimiter() {
        System.out.println("############ This is ratelimiter demo ############");
        return new ResponseEntity<String>("This is Ratelimiter Demo method", HttpStatus.OK);
    }

    @GetMapping("/bulkhead")
    @Bulkhead(name = "mycalls") // http://localhost:9094/api/resilience4j/bulkhead
    public ResponseEntity<String> bulkhead()throws Exception {
        Thread.sleep(100);
        System.out.println("############ This is bulkhead demo ############");
        return new ResponseEntity<String>("This is bulkhead Demo method", HttpStatus.OK);
    }

    public ResponseEntity<String> showError(Exception ex) {
        log.info("###### This is default Response ####");
        return new ResponseEntity<String>("This is default response", HttpStatus.OK);
    }

    public Map<String,Object> getData(Boolean isException){
        Map<String,Object> data = new LinkedHashMap<>();
        data.put("Message","Vetri Shiva");
        if(true)
            throw new RuntimeException("Unable to get data");
        return data;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(3000);
                System.out.println("Hello World");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
