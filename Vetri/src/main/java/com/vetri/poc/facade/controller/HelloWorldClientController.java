package com.vetri.poc.facade.controller;

import com.vetri.poc.facade.client.remedy.ExternalRestApiFacade;
import com.vetri.poc.facade.config.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class HelloWorldClientController {


    @Value("${jwt.user}")
    private String user;
    @Value("${jwt.password}")
    private String password;

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    ExternalRestApiFacade externalRestApiFacade;

    @RequestMapping({ "/client/hello" })
    public String firstPage() {
        if(externalRestApiFacade == null)
            externalRestApiFacade = new ExternalRestApiFacade(user,password,restTemplate,jwtTokenUtil);
        return externalRestApiFacade.getHelloResource();
    }

}
