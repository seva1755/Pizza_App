package com.example.pizza;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class PizzaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PizzaApplication.class, args);
	}

}
