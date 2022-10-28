package com.sparta.coding_galaxy_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CodingGalaxyBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodingGalaxyBeApplication.class, args);
	}

}
