package org.dante.saga.springboot.pay;

import org.apache.servicecomb.pack.omega.spring.EnableOmega;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableOmega
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class SpiritPayApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpiritPayApplication.class, args);
	}

}
