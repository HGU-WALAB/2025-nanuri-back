package com.walab.nanuri;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NanuriApplication {

	public static void main(String[] args) {
		SpringApplication.run(NanuriApplication.class, args);
	}

}
