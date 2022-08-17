package com.tp.pry20220169;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Pry20220169Application {

	public static void main(String[] args) {
		SpringApplication.run(Pry20220169Application.class, args);
	}

	@Bean
	public ModelMapper modelMapper() { return new ModelMapper(); }
}
