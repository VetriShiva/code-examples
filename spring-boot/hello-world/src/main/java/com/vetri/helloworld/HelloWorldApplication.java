package com.vetri.helloworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class HelloWorldApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloWorldApplication.class, args);
	}

}

@RestController
class HomeController{

	@GetMapping("/")
	public String index(String name){
		return "Hello World!";
	}

	@GetMapping("/echo")
	public String echo(String name){
		return name;
	}

	@GetMapping("/hello")
	public String hello(String name){
		return "Hello "+name;
	}
}
