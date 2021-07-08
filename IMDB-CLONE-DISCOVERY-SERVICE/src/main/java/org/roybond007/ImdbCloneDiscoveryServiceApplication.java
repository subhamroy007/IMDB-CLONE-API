package org.roybond007;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class ImdbCloneDiscoveryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImdbCloneDiscoveryServiceApplication.class, args);
	}

}
