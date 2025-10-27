package com.ms1Service.ms1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Ms1ServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(Ms1ServiceApplication.class, args);
	}

}
