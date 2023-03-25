package com.vetri.poc.facade;

import com.vetri.poc.facade.config.LoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class FacadePatternApplication {
	static Logger LOGGER = LoggerFactory.getLogger(FacadePatternApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(FacadePatternApplication.class, args);
	}

	@Bean
	public RestTemplate getRestTemplate() {
//		ClientHttpRequestFactory factory =
//				new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
//		RestTemplate restTemplate = new RestTemplate(factory);

		/*
			use a buffered RestTemplate instance only if DEBUG level is enabled
		*/
		RestTemplate restTemplate = null;
		if (LOGGER.isDebugEnabled()) {
			SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
			requestFactory.setOutputStreaming(false);
			ClientHttpRequestFactory factory
					= new BufferingClientHttpRequestFactory(requestFactory);
			restTemplate = new RestTemplate(factory);
		} else {
			restTemplate = new RestTemplate();
		}

		List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
		if (CollectionUtils.isEmpty(interceptors)) {
			interceptors = new ArrayList<>();
		}
		interceptors.add(new LoggingInterceptor());
		restTemplate.setInterceptors(interceptors);

		restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
			public boolean hasError(ClientHttpResponse response) throws IOException {
				HttpStatus statusCode = response.getStatusCode();
				return statusCode.series() == HttpStatus.Series.SERVER_ERROR;
			}
		});

		return restTemplate;
	}
}
