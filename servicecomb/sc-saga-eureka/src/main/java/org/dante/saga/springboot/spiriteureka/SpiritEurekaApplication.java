package org.dante.saga.springboot.spiriteureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class SpiritEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpiritEurekaApplication.class, args);
	}

}
