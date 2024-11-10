package com.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableAsync
@EnableCaching
@EnableWebMvc
public class FoodManiaApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodManiaApplication.class, args);
	}

}
