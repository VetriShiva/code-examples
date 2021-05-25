package com.vetri.poc.hzcache.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController{

	@RequestMapping("/")
	public String index(){
		return "cache demo";
	}
}
