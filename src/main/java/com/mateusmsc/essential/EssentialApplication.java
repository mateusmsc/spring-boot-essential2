package com.mateusmsc.essential;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class EssentialApplication {

	public static void main(String[] args) {
		SpringApplication.run(EssentialApplication.class, args);
	}

}
