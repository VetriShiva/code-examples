package com.vetri.poc.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class,

		SecurityAutoConfiguration.class,  ManagementWebSecurityAutoConfiguration.class
})
public class KafkaPocApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaPocApplication.class, args);
	}

}
