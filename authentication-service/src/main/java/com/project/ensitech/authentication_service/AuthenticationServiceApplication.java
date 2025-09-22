package com.project.ensitech.authentication_service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Objects;

@SpringBootApplication
// @EnableEurekaClient
public class AuthenticationServiceApplication {

	/*public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		System.setProperty("DB_URL", Objects.requireNonNull(dotenv.get("DB_URL")));
		System.setProperty("DB_USERNAME", Objects.requireNonNull(dotenv.get("DB_USERNAME")));
		System.setProperty("DB_PASSWORD", Objects.requireNonNull(dotenv.get("DB_PASSWORD")));
		System.setProperty("JWT_SECRET_KEY", Objects.requireNonNull(dotenv.get("JWT_SECRET_KEY")));
		System.out.println("DB_USER: " + dotenv.get("DB_USER"));

		SpringApplication.run(AuthenticationServiceApplication.class, args);
	}*/

	public static void main(String[] args) {
		String dbUrl, dbUsername, dbPassword, jwtSecretKey;

		try {
			// Tentative de lecture via .env (utile en local)
			Dotenv dotenv = Dotenv.load();

			dbUrl = Objects.requireNonNull(dotenv.get("DB_URL"));
			dbUsername = Objects.requireNonNull(dotenv.get("DB_USERNAME"));
			dbPassword = Objects.requireNonNull(dotenv.get("DB_PASSWORD"));
			jwtSecretKey =  Objects.requireNonNull(dotenv.get("JWT_SECRET_KEY"));



			System.out.println("Chargement des variables depuis .env ou System.getenv() terminé ✅");

		} catch (Exception e) {
			// fallback en cas de souci avec dotenv
			System.out.println(" Impossible de charger .env, on utilise uniquement System.getenv()");
			dbUrl = System.getenv("DB_URL");
			dbUsername = System.getenv("DB_USERNAME");
			dbPassword = System.getenv("DB_PASSWORD");
			jwtSecretKey = System.getenv("JWT_SECRET_KEY");
		}

		// Vérification des valeurs
		if (dbUrl == null || dbUsername == null || dbPassword == null || jwtSecretKey == null) {
			throw new IllegalStateException("Variables manquantes (DB_URL, DB_USERNAME, DB_PASSWORD, JWT_SECRET_KEY)");
		}

		// Injection dans Spring
		System.setProperty("DB_URL", dbUrl);
		System.setProperty("DB_USERNAME", dbUsername);
		System.setProperty("DB_PASSWORD", dbPassword);
		System.setProperty("JWT_SECRET_KEY", jwtSecretKey);

		SpringApplication.run(AuthenticationServiceApplication.class, args);
	}

}
