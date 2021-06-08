package com.vetri.poc.aop;

import com.vetri.poc.aop.service.DomainService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AopApplicationTest {

    @Autowired
    private DomainService service;

    @Test
    public void testGetDomainObjectById()
    {
        service.getDomainObjectById(10L);
    }
}