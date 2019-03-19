package org.dante.saga.springboot.account;

import org.apache.servicecomb.pack.omega.spring.EnableOmega;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableOmega
@EnableDiscoveryClient
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class SpiritAccountApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SpiritAccountApplication.class, args);
	}

}
