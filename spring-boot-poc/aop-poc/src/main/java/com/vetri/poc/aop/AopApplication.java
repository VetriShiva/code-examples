package com.vetri.poc.aop;

import com.vetri.poc.aop.service.ExceptionalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Slf4j
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,

        SecurityAutoConfiguration.class
})
@EnableAspectJAutoProxy
public class AopApplication implements CommandLineRunner {

    @Autowired
    ExceptionalService service;

    @Override
    public void run(String... args) throws Exception {
        try {
            log.info("call service method");
            service.throwException("001");
        } catch (Exception ex) {
            // Do nothing Since
        }
    }
    public static void main(String[] args) {
        SpringApplication.run(AopApplication.class, args);
    }
}
