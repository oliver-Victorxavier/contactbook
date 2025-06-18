package com.victorxavier.contactbook;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@EnableFeignClients
@SpringBootApplication
@OpenAPIDefinition( info =
@Info(title = "Contact Book", description = "API responsible for managing contacts", version = "1"))
public class ContactbookApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContactbookApplication.class, args);
	}

}
