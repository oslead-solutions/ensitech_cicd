package com.project.user_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


/**
 * Classe de configuration principale pour Spring Security.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Définit un bean PasswordEncoder qui peut être injecté dans toute l'application.
     * Nous utilisons BCrypt, qui est l'algorithme de hachage standard et sécurisé.
     * @return une instance de BCryptPasswordEncoder.
     */
    /**
     * Bean pour le hachage des mots de passe.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * NOUVELLE MÉTHODE : C'est ici qu'on configure les règles d'accès.
     * Ce bean va remplacer la configuration par défaut de Spring Security.
     *
     * @param http L'objet HttpSecurity à configurer.
     * @return La chaîne de filtres de sécurité construite.
     * @throws Exception en cas d'erreur de configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Étape 1 : Désactiver la protection CSRF.
                // C'est une pratique courante pour les APIs REST "stateless".
                .csrf(csrf -> csrf.disable())

                // Étape 2 : Définir les règles d'autorisation pour les requêtes HTTP.
                .authorizeHttpRequests(auth -> auth
                        // Pour le moment, pendant le développement, on autorise TOUT.
                        // ".anyRequest()" = pour n'importe quelle requête
                        // ".permitAll()" = autoriser sans authentification
                        .anyRequest().permitAll()
                );

        // On construit et on retourne la configuration.
        return http.build();
    }
}
