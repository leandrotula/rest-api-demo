package com.app.ws.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class AppWsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppWsApplication.class, args);
	}

}
