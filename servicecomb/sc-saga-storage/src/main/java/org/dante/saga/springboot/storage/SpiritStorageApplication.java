package org.dante.saga.springboot.storage;

import org.apache.servicecomb.pack.omega.spring.EnableOmega;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableOmega
@EnableDiscoveryClient
@SpringBootApplication
public class SpiritStorageApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpiritStorageApplication.class, args);
	}

}
