package com.vetri.poc.kafka;

import com.vetri.poc.kafka.model.ConsumerInfoList;
import com.vetri.poc.kafka.service.CommonGenericKafkaListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;

@Slf4j
@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class,

		SecurityAutoConfiguration.class,  ManagementWebSecurityAutoConfiguration.class
})
public class KafkaPocApplication {

	@Autowired
	private ConsumerInfoList consumerInfoList;

	@Autowired
	private BeanFactory beanFactory;

	public static void main(String[] args) {
		SpringApplication.run(KafkaPocApplication.class, args);
	}

	@PostConstruct
	private void init(){
		log.info("Init Block");
		log.info("consumerInfoList: {}",consumerInfoList);

		Assert.state(beanFactory instanceof ConfigurableBeanFactory, "wrong bean factory type");
		ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory)beanFactory;

		consumerInfoList.getConfigurations().stream().forEach(consumerInfo -> {
			// register bean per listener
			CommonGenericKafkaListener commonGenericKafkaListener = new CommonGenericKafkaListener(consumerInfo);
			configurableBeanFactory.registerSingleton(consumerInfo.getListenerBeanName(),commonGenericKafkaListener);
			log.info("{} - Bean has been registered",consumerInfo.getListenerBeanName());
		});
	}

	@Bean
	MappingJackson2MessageConverter consumerJackson2MessageConverter(){
		return new MappingJackson2MessageConverter();
	}

	@Bean
	MessageHandlerMethodFactory messageHandlerMethodFactory(){
		DefaultMessageHandlerMethodFactory messageHandlerMethodFactory = new DefaultMessageHandlerMethodFactory();
		messageHandlerMethodFactory.setMessageConverter(consumerJackson2MessageConverter());
		return messageHandlerMethodFactory;
	}
}
