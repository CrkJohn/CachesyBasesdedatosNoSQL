package edu.eci.arsw.cinema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"edu.eci.arsw.cinema"})
public class CinemaAPIApplication {

	public static void main(String[] args) {
		SpringApplication.run(CinemaAPIApplication.class, args);
	}

}
