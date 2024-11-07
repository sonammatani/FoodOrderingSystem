package com.project.foodmania;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FoodManiaApplication {

	public static void main(String[] args) {
		SpringApplication.run(com.project.foodmania.FoodManiaApplication.class, args);
	}

}
