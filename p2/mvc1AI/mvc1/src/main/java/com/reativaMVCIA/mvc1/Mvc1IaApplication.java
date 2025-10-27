package com.reativaMVCIA.mvc1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Mvc1IaApplication {

	public static void main(String[] args) {
		SpringApplication.run(Mvc1IaApplication.class, args);
	}

}
