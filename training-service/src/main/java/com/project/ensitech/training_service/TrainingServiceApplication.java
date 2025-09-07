package com.project.ensitech.training_service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.Objects;

@SpringBootApplication
@EnableFeignClients
public class TrainingServiceApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		/*Dotenv dotenv = Dotenv.configure()
				.directory("./") // ou chemin relatif depuis le dossier où tu exécutes ton programme
				.load();*/
		System.setProperty("DB_URL", Objects.requireNonNull(dotenv.get("DB_URL")));
		System.setProperty("DB_USERNAME", Objects.requireNonNull(dotenv.get("DB_USERNAME")));
		System.setProperty("DB_PASSWORD", Objects.requireNonNull(dotenv.get("DB_PASSWORD")));


		SpringApplication.run(TrainingServiceApplication.class, args);
	}

}
