package com.project.gateway_service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.Objects;


@SpringBootApplication
@EnableDiscoveryClient

public class GatewayServiceApplication {

	/*public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		System.setProperty("JWT_SECRET_KEY", Objects.requireNonNull(dotenv.get("JWT_SECRET_KEY")));
		SpringApplication.run(GatewayServiceApplication.class, args);
	}*/
	public static void main(String[] args) {
		String jwtSecretKey;

		try {
			// Tentative de lecture via .env (utile en local)
			Dotenv dotenv = Dotenv.load();
			jwtSecretKey =  Objects.requireNonNull(dotenv.get("JWT_SECRET_KEY"));

			System.out.println("Chargement des variables depuis .env ou System.getenv() terminé ");

		} catch (Exception e) {
			// fallback en cas de souci avec dotenv
			System.out.println(" Impossible de charger .env, on utilise uniquement System.getenv()");

			jwtSecretKey = System.getenv("JWT_SECRET_KEY");
		}

		// Vérification des valeurs
		if ( jwtSecretKey == null) {
			throw new IllegalStateException("Variables manquantes (JWT_SECRET_KEY)");
		}

		// Injection dans Spring

		System.setProperty("JWT_SECRET_KEY", jwtSecretKey);

		SpringApplication.run(GatewayServiceApplication.class, args);
	}

}
