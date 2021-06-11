package com.vetri.poc.aop;

import com.vetri.poc.aop.service.DomainService;
import com.vetri.poc.aop.service.ExceptionalService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class AopApplicationTests {

	@Autowired
	private DomainService domainService;

	@Autowired
	ExceptionalService exceptionalService;

	@Test
	void contextLoads() {
	}

	@Test
	public void testGetDomainObjectById()
	{
		domainService.getDomainObjectById(10L);
	}

	@SneakyThrows
	@Test
	public void testAopExceptionFlow(){
		exceptionalService.throwException("001");
	}
}
