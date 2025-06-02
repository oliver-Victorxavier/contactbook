package com.victorxavier.contactbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@EnableFeignClients
@SpringBootApplication
public class ContactbookApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContactbookApplication.class, args);
	}

}
