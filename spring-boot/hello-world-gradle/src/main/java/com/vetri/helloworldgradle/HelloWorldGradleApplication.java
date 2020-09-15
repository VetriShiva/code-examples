package com.vetri.helloworldgradle;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PreDestroy;

/*
https://www.baeldung.com/spring-boot-shutdown
 */
@SpringBootApplication
public class HelloWorldGradleApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloWorldGradleApplication.class, args);
    }

    @PreDestroy
    public void onDestroy() throws Exception {
        System.out.println("Spring Container is destroyed!");
    }

    private static void closeApplication() {

        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(HelloWorldGradleApplication.class).web(WebApplicationType.NONE).run();
        System.out.println("Spring Boot application started");
        ctx.getBean(HelloWorldGradleApplication.class);
        ctx.close();
    }

    private static void exitApplication() {

        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(HelloWorldGradleApplication.class).web(WebApplicationType.NONE).run();

        int exitCode = SpringApplication.exit(ctx, () -> {
            // return the error code
            return 0;
        });

        System.out.println("Exit Spring Boot");

        System.exit(exitCode);
    }

    private static void writePID() {
        SpringApplicationBuilder app = new SpringApplicationBuilder(HelloWorldGradleApplication.class).web(WebApplicationType.NONE);
        app.build().addListeners(new ApplicationPidFileWriter("./bin/shutdown.pid"));
        app.run();
    }
}

@RestController
class HomeController {

    @GetMapping("/")
    public String index(String name) {
        return "Hello World!";
    }

    @GetMapping("/echo")
    public String echo(String name) {
        return "echo " + name;
    }

    @GetMapping("/hello")
    public String hello(String name) {
        return "Hello " + name;
    }
}

@RestController
class ShutdownController implements ApplicationContextAware {

    private ApplicationContext context;

    @GetMapping("/shutdownContext")
    public void shutdownContext() {
        ((ConfigurableApplicationContext) context).close();
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.context = ctx;
    }
}
