package com.restfulapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class RestfulapiApplication {
	public static final Logger log = LoggerFactory.getLogger(RestfulapiApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(RestfulapiApplication.class, args);
		log.debug("It works!");
	}
}
