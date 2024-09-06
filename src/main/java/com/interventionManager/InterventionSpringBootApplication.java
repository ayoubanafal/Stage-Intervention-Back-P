package com.interventionManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InterventionSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(InterventionSpringBootApplication.class, args);
	}

}
