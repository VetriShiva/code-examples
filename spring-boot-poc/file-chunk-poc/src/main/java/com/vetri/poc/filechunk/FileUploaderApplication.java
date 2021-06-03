package com.vetri.poc.filechunk;

import com.vetri.poc.filechunk.config.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class,

		SecurityAutoConfiguration.class,  ManagementWebSecurityAutoConfiguration.class
})
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class FileUploaderApplication {

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*").allowedMethods("OPTIONS", "PUT", "DELETE", "GET",
						"POST").allowedHeaders("X-requested-with",
						"Content-Type", "Origin","Content-Range","Range",
						"authorization",
						"x-upload-content-length",
						"x-upload-content-type",
						"location");
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(FileUploaderApplication.class, args);
	}

}
