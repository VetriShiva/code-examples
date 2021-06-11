package com.vetri.poc.aop;

import com.vetri.poc.aop.service.DomainService;
import com.vetri.poc.aop.service.ExceptionalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
    https://howtodoinjava.com/spring-boot2/aop-aspectj/
 */

@Slf4j
@SpringBootApplication
public class AopApplication implements CommandLineRunner {

	@Autowired
	private DomainService domainService;

	@Autowired
	ExceptionalService exceptionalService;

	public static void main(String[] args) {
		SpringApplication.run(AopApplication.class, args);
		log.info("VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV");
		log.info("VVV AOP POC Successfully Started VVV");
		log.info("VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV");

	}

	@Override
	public void run(String... args) throws Exception {
		try {
			log.info("call domainService method");
			domainService.getDomainObjectById(100L);
			log.info("call exceptionalService method");
			exceptionalService.throwException("001");
		} catch (Exception ex) {
			// Do nothing Since
		}
	}
}
