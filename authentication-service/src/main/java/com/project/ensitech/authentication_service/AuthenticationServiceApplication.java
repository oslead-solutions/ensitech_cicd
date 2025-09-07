package com.project.ensitech.authentication_service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Objects;

@SpringBootApplication
// @EnableEurekaClient
public class AuthenticationServiceApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		/*Dotenv dotenv = Dotenv.configure()
				.directory("./") // ou chemin relatif depuis le dossier où tu exécutes ton programme
				.load();*/
		System.setProperty("DB_URL", Objects.requireNonNull(dotenv.get("DB_URL")));
		System.setProperty("DB_USERNAME", Objects.requireNonNull(dotenv.get("DB_USERNAME")));
		System.setProperty("DB_PASSWORD", Objects.requireNonNull(dotenv.get("DB_PASSWORD")));
		System.setProperty("JWT_SECRET_KEY", Objects.requireNonNull(dotenv.get("JWT_SECRET_KEY")));
		System.out.println("DB_USER: " + dotenv.get("DB_USER"));

		SpringApplication.run(AuthenticationServiceApplication.class, args);
	}

}
